package cn.com.yusong.yhdg.common.tool.sql;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseFile {

    Pattern pattern = Pattern.compile("(\\w+)");
    Pattern underScorePattern = Pattern.compile("([A-Za-z0-9]+)");


    public List<Table> parse(File file) throws IOException {
        List<Table> list = new ArrayList<Table>();
        List<String> lines = FileUtils.readLines(file, "UTF-8");

        Table table = null;
        for(String line : lines) {
            line = StringUtils.trim(line);
            if(line.isEmpty()) {
                continue;
            }

            if(line.equals("/*create table end*/")) {
                break;
            }

            if(table == null) {
                table = new Table();
            }

            if(line.startsWith("/*")) {
                table.setDescription(StringUtils.substringBetween(line, "/*", "*/"));
            } else if(line.startsWith("create table")) {
                table.setName(StringUtils.substringBetween(line, "create table", "(").trim());

                Matcher matcher = underScorePattern.matcher(table.getName());
                StringBuffer buffer = new StringBuffer();
                while (matcher.find()) {
                    buffer.append(StringUtils.capitalize(matcher.group(1)));
                }
                String name = buffer.toString().replace("Shfw", "");
                table.setJavaName(name);

            } else if(line.startsWith("primary key")) {

            } else if(line.startsWith(")")) {
                list.add(table);
                table = null;

            } else { //column line
                Column column = new Column();
                column.setDescription(StringUtils.substringBetween(line, "/*", "*/"));
                if(StringUtils.isEmpty(column.getDescription())) {
                    column.setDescription("");
                }

                Matcher matcher = pattern.matcher(line);
                if(matcher.find()) {
                    column.setName(matcher.group(1));
                    matcher = underScorePattern.matcher(column.getName());
                    StringBuffer buffer = new StringBuffer();
                    while (matcher.find()) {
                        if(buffer.length() == 0) {
                            buffer.append(matcher.group(1));
                        } else {
                            buffer.append(StringUtils.capitalize(matcher.group(1)));
                        }
                    }
                    column.setJavaName(buffer.toString());
                }

                column.setType(line.replaceAll("/\\*.*\\*/", "").replaceAll(column.name + " ", "").replaceAll(",", "").trim());
                if(column.getType().contains("primary key") && column.getType().contains("number")) {
                    table.setSuperJavaName("LongIdEntity");
                } else if(column.getType().contains("primary key") && column.getType().contains("char")) {
                    table.setSuperJavaName("StringIdEntity");
                }

                if(column.getType().contains("number(22)")) {
                    column.setJavaType("Long");
                } else if(column.getType().contains("number")) {
                    column.setJavaType("Integer");
                } else if(column.getType().contains("date")) {
                    column.setJavaType("Date");
                } else if(column.getType().contains("char") || column.getType().contains("clob")) {
                    column.setJavaType("String");
                }

                table.getColumnList().add(column);
            }

        }

        return list;
    }

    public void genWord(File templateFile, List<Table> tableList, File outFile) throws IOException, TemplateException {
        Configuration config = new Configuration();
        config.setDirectoryForTemplateLoading(templateFile.getParentFile());
        config.setDefaultEncoding("UTF-8");

        Template template = config.getTemplate(templateFile.getName());

        StringWriter writer = new StringWriter();

        Map map = new HashMap();
        map.put("tableList", tableList);
        template.process(map, writer);

        String xml = writer.toString();
        FileUtils.writeStringToFile(outFile, xml, "UTF-8");
    }

    public static void main(String[] args) throws Exception {

        String dbInFile = "";
        String wordTemplateFile = "";
        String wordOutFile = "";

        if(args.length != 3) {
            System.out.println("格式: dbInFile wordTemplateFile wordOutFile");
            System.exit(0);
        }

        File sqlFile = new File(dbInFile);
        File templateFile = new File(wordTemplateFile);
        File outFile = new File(wordOutFile);

        ParseFile parser = new ParseFile();
        List<Table> tableList = parser.parse(sqlFile);
        parser.genWord(templateFile, tableList, outFile);
    }

}
