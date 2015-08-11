package org.thousandturtles.asciidoctorj_demo;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.ast.ContentPart;
import org.asciidoctor.ast.StructuredDocument;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Chiaki Chikame on 8/11/15.
 * <p>
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/txt/copying/ for more details.
 */
class Main {
    static String makeLevelIndent(int level) {
        StringBuilder sb = new StringBuilder(level);
        for(int i = 0; i < level; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    static void traverseParts(List<ContentPart> parts, int level) {
        String levelStr = makeLevelIndent(level);
        for(ContentPart part: parts) {
            System.out.printf("%s============%n", levelStr);
            System.out.printf("%s%s: %s%n", levelStr, "ID", part.getId());
            System.out.printf("%s%s: %s%n", levelStr, "Title", part.getTitle());
            System.out.printf("%s%s: %s%n", levelStr, "Context", part.getContext());
            System.out.printf("%s%s%n%s%n", levelStr, "Content", part.getContent());
            List<ContentPart> innerParts = part.getParts();
            if (innerParts != null && !innerParts.isEmpty()) {
                traverseParts(innerParts, level + 1);
            }
            System.out.printf("%s============%n", levelStr);
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.setBackend("html5");
        Map<String, Object> map = options.map();
        Asciidoctor doctor = Asciidoctor.Factory.create();

        System.out.println("\n\nThe following results shows how to convert part by part ..");
        System.out.println("See src/main/resources/test.adoc for asciidoc source");
        InputStream inputStream = Main.class.getResourceAsStream("/test.adoc");
        InputStreamReader reader = new InputStreamReader(inputStream);
        StructuredDocument document = doctor.readDocumentStructure(reader, map);
        List<ContentPart> parts = document.getParts();
        traverseParts(parts, 0);

        Consumer<String> convertAndPrint = adocStr -> System.out.println(doctor.convert(adocStr, map));
        System.out.println("\n\nThe following results shows how to convert fragments directly!");
        System.out.println("=================");
        convertAndPrint.accept("== GGC user guide");
        System.out.println("=================");
        convertAndPrint.accept("This is an `asciidoc` fragment!!");
        System.out.println("=================");
        convertAndPrint.accept("[source,java]\n----\nSystem.out.println(\"Hello world!\");\n----\n");
        System.out.println("=================");

        System.out.println("We can parse the html code generated, or use them directly.");
    }
}
