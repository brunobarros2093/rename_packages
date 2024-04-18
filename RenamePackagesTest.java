import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.*;

public class RenamePackagesTest {

    private static final String TEST_DIRECTORY = "/caminho/do/seu/projeto/test";
    private static final String OLD_PACKAGE_1 = "com.example.oldpackage1";
    private static final String OLD_PACKAGE_2 = "com.example.oldpackage2";
    private static final String NEW_PACKAGE = "com.example.newpackage";
    private static final String PACKAGE_DESCRIPTION = "This is the new package.";

    @Before
    public void setup() {
        // Criação de arquivos de teste
        try {
            File testDirectory = new File(TEST_DIRECTORY);
            if (!testDirectory.exists()) {
                testDirectory.mkdir();
            }

            File package1Dir = new File(TEST_DIRECTORY + "/com/example/oldpackage1");
            package1Dir.mkdirs();
            File package1File = new File(package1Dir, "TestClass1.java");
            FileWriter writer1 = new FileWriter(package1File);
            writer1.write("package com.example.oldpackage1;\n\npublic class TestClass1 {}");
            writer1.close();

            File package2Dir = new File(TEST_DIRECTORY + "/com/example/oldpackage2");
            package2Dir.mkdirs();
            File package2File = new File(package2Dir, "TestClass2.java");
            FileWriter writer2 = new FileWriter(package2File);
            writer2.write("package com.example.oldpackage2;\n\npublic class TestClass2 {}");
            writer2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRenamePackages() {
        List<String> oldPackages = Arrays.asList(OLD_PACKAGE_1, OLD_PACKAGE_2);
        RenamePackages.renamePackages(TEST_DIRECTORY, oldPackages, NEW_PACKAGE, PACKAGE_DESCRIPTION);

        // Verificar se os pacotes antigos foram renomeados corretamente para o novo pacote
        File newPackageDir1 = new File(TEST_DIRECTORY + "/com/example/newpackage");
        Assert.assertTrue(newPackageDir1.exists());
        File newPackageFile1 = new File(newPackageDir1, "TestClass1.java");
        Assert.assertTrue(newPackageFile1.exists());

        File newPackageDir2 = new File(TEST_DIRECTORY + "/com/example/newpackage");
        Assert.assertTrue(newPackageDir2.exists());
        File newPackageFile2 = new File(newPackageDir2, "TestClass2.java");
        Assert.assertTrue(newPackageFile2.exists());

        // Verificar se a descrição do pacote foi adicionada corretamente
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader(newPackageFile1));
            String line1 = reader1.readLine();
            Assert.assertTrue(line1.contains("This is the new package."));
            reader1.close();

            BufferedReader reader2 = new BufferedReader(new FileReader(newPackageFile2));
            String line2 = reader2.readLine();
            Assert.assertTrue(line2.contains("This is the new package."));
            reader2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        // Limpeza dos arquivos de teste
        File testDirectory = new File(TEST_DIRECTORY);
        deleteDirectory(testDirectory);
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}