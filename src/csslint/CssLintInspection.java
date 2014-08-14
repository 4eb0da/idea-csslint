package csslint;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElementVisitor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class CssLintInspection extends LocalInspectionTool {
    private Path csslintPath;

    public CssLintInspection() {
        super();

        csslintPath = CssLintDataProvider.getInstance().getPath();
    }

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitFile (com.intellij.psi.PsiFile file) {
                super.visitFile(file);

                if (!file.getName().endsWith(".css")) {
                    return;
                }

                final ASTNode node = file.getNode();

                if (node == null) {
                    return;
                }

                String text = file.getText();
                if (text.length() == 0) {
                    return;
                }

                Document document = PsiDocumentManager.getInstance(file.getProject()).getCachedDocument(file);
                CssLintThread thread = new CssLintThread(csslintPath, text);
                thread.start();

                try {
                    thread.join(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String output;
                try {
                    output = thread.getOutput();
                } catch (CssLintThread.CssLintThreadException e) {
                    return;
                }

                ArrayList<CssLintIssue> issues = parseOutput(output, document);

                for (CssLintIssue issue : issues) {
                    holder.registerProblem(
                        holder.getManager().createProblemDescriptor(
                            file,
                            issue.getRange(),
                            "CSSLint: " + issue.getMessage(),
                            issue.getType() == CssLintIssue.Type.Error ? ProblemHighlightType.ERROR : ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            isOnTheFly
                        )
                    );
                }
            }
        };
    }

    private ArrayList<CssLintIssue> parseOutput(String output, Document document) {
        ArrayList<CssLintIssue> issues = new ArrayList<CssLintIssue>();
        String res[] = output.split("\n");

        for (String str : res) {
            String part = str.substring(str.indexOf(":") + 2);
            String[] parts = part.split(", ");
            if (parts.length < 3) {
                continue;
            }
            int line = Integer.parseInt(parts[0].substring(5));
            int col = Integer.parseInt(parts[1].substring(4));

            String detail = StringUtils.join(Arrays.copyOfRange(parts, 2, parts.length));
            CssLintIssue.Type type;
            if (detail.substring(0, 5).equals("Error")) {
                type = CssLintIssue.Type.Error;
            } else {
                type = CssLintIssue.Type.Warning;
            }

            String message = detail.substring(detail.indexOf("-") + 2);
            int offset = document.getLineStartOffset(line - 1) + col - 1;
            TextRange range = new TextRange(offset, document.getLineEndOffset(line - 1));

            issues.add(new CssLintIssue(range, type, message));
        }

        return issues;
    }
}
