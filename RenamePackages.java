import java.io.*;
import java.util.*;
import java.util.regex.*;

public class RenamePackages {

    public static void renamePackages(String path, List<String> oldPackages, String newPackageName, String packageDescription) {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("O caminho fornecido não existe ou não é um diretório.");
            return;
        }

        List<File> javaFiles = new ArrayList<>();
        findJavaFiles(directory, javaFiles);

        String importRegex = "import\\s+(?:" + String.join("|", oldPackages) + ")\\.";
        String packageRegex = "package\\s+(?:" + String.join("|", oldPackages) + ");";

        for (File javaFile : javaFiles) {
            String relativePath = javaFile.getParentFile().toPath().relativize(directory.toPath()).toString().replace(File.separator, ".");
            String newPackagePath = newPackageName + "." + relativePath;

            try {
                BufferedReader reader = new BufferedReader(new FileReader(javaFile));
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
                reader.close();

                String fileData = fileContent.toString();

                fileData = fileData.replaceAll(importRegex, "import " + newPackagePath + ".");
                fileData = fileData.replaceAll(packageRegex, "package " + newPackagePath + ";");

                if (packageDescription != null) {
                    fileData = "/**\n * " + packageDescription + "\n */\n" + fileData;
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile));
                writer.write(fileData);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("O nome do pacote foi alterado com sucesso em todas as classes Java.");
    }

    private static void findJavaFiles(File directory, List<File> javaFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findJavaFiles(file, javaFiles);
                } else if (file.isFile() && file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
    }

    public static void main(String[] args) {
        String path = "/caminho/do/seu/projeto";
        List<String> oldPackages = Arrays.asList("pacote.antigo1", "pacote.antigo2");
        String newPackageName = "pacote.novo";
        String packageDescription = "Este é o novo pacote.";

        renamePackages(path, oldPackages, newPackageName, packageDescription);
    }
}
