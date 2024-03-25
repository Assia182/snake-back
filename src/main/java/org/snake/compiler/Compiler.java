package org.snake.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Compiler {

    public static void main(String[] args) {
        generateClassFromFolder("./snake-back/src/main/java/org/snake/compiler/toCompile");
    }




    public static void generateClassFromFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            StringBuilder classBuilder = new StringBuilder();
            classBuilder.append("package org.snake.compiler;\nimport org.snake.model.SnakeModel;\npublic class GeneratedClass {\n");

            for (File file : files) {
                System.out.println(file.getPath());
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String methodName = file.getName().replace(".txt", "");
                    String methodContent = generateMethod(file);
                    classBuilder.append(methodContent).append("\n");
                }
            }

            classBuilder.append("}");

            String classContent = classBuilder.toString();
            System.out.println("Generated class:\n" + classContent);
            // Optionally write the class content to a file
             writeToFile(classContent, "snake-back/src/main/java/org/snake/compiler/GeneratedClass.java");
        } else {
            System.err.println("Folder does not exist or is empty.");
        }
    }



    // Optional method to write the generated class content to a file
    public static void writeToFile(String content, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            /*for (int i = 0; i<content.length(); i++){
                fileWriter.append(content.charAt(i));
            }*/fileWriter.write(content);
            fileWriter.close();

            System.out.println("Generated class written to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




public static String generateMethod(File file) {
        String res = "";
        try {
            Scanner scanner = new Scanner(file);
            StringBuilder methodBuilder = new StringBuilder();

            // Method name based on file name
            String methodName = file.getName().replace(".txt", "");
            methodBuilder.append("public static void ").append(methodName).append("(SnakeModel snake, int size){\n");

            // Build method content
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.contains("right")) {
                    methodBuilder.append("snake.addPosX(size);\n");
                } else if (line.contains("left")) {
                    methodBuilder.append("snake.addPosX(-size);\n");
                }
            }
            methodBuilder.append("}");
            res = methodBuilder.toString();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;


    }




}
