package com.algorithm.learn.test.unit1.everything.mzzc;

import com.algorithm.learn.test.unit1.util.ListAllFiles;
import com.google.common.base.CaseFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author : Chen Changjiang
 * @date : 2019/12/13
 * @description :
 */
public class MzzcTextBuilder {

    public static void resolveNotRemarkedEnum() {
        String string = "        /**\n" +
                "         * 男\n" +
                "         */\n" +
                "        MALE,\n" +
                "        /**\n" +
                "         * 女\n" +
                "         */\n" +
                "        FEMALE;";
        String[] split = string.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split("         * ");
            stringBuilder.append("        /**\n" +
                    "         * " + split1[1].substring("*".length()).trim() + "\n" +
                    "         */\n");
            stringBuilder.append("@Remark(\"" + split1[1].substring("*".length()).trim() + "\")\n" + split1[2].substring("*/".length()).trim() + ",\n");
        }
        String substring = stringBuilder.substring(0, stringBuilder.length() - 2);
        System.out.println(substring.endsWith(";") ? substring : substring + ";");
    }

    public static void buildCRUDForMZZCControllerByRequestMappingPath(String paramPath) {
        String[] strings = paramPath.split("/");
        String basePath = strings[strings.length - 1];
        String groupPath = strings[strings.length - 2];
        String facadeRef = basePath + "Facade";
        String facadeClass = facadeRef.substring(0, 1).toUpperCase() + facadeRef.substring(1);
        String entityRef = basePath;
        String entityClass = entityRef.substring(0, 1).toUpperCase() + entityRef.substring(1);
        String controllerName = entityClass + "Controller";
        System.out.println("import com.jf.platform.core.query.Search;\n" +
                "import com.jf.platform.core.web.util.SearchUtil;\n" +
                "import com.jf.platform.status.OperateStatus;\n" +
                "import io.swagger.annotations.Api;\n" +
                "import io.swagger.annotations.ApiOperation;\n" +
                "import org.apache.dubbo.config.annotation.Reference;\n" +
                "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                "import org.springframework.web.bind.annotation.ResponseBody;\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import com.jf.platform.core.query.PageResult;" +
                "import org.springframework.stereotype.Controller;\n" +
                "import org.springframework.web.bind.annotation.RequestMapping;");
        System.out.println("/**\n" +
                "* @author Chen Changjiang\n" +
                "* @date 2020-01-04 13:43:01\n" +
                "* @description :\n" +
                "*/");
        System.out.println("@Controller\n" +
                "@RequestMapping(value = \"" + paramPath + "\")\n" +
                "@Api(value = \"API.VALUE\", tags = \"xxx接口\")\n" +
                "public class " + controllerName + " {");

        System.out.println();
        System.out.println("    @Reference(version = \"1.0\", group = \"" + groupPath + "\")\n" +
                "    private " + facadeClass + " " + facadeRef + ";");
        System.out.println();
        System.out.println("    @RequestMapping(value = \"/index\", method = RequestMethod.GET)\n" +
                "    @ApiOperation(value = \"跳转index页面\", notes = \"NOTES\")\n" +
                "    public String index() {\n" +
                "        return \"" + paramPath + "/index\";\n" +
                "    }");
        System.out.println();
        System.out.println("    @ResponseBody\n" +
                "    @RequestMapping(value = \"/listAll\", method = RequestMethod.POST)\n" +
                "    @ApiOperation(value = \"分页展示\", notes = \"NOTES\")\n" +
                "    public OperateStatus listAll(HttpServletRequest request) {\n" +
                "        Search search = SearchUtil.genSearch(request);\n" +
                "        PageResult<" + entityClass + "> pageResult = " + facadeRef + ".findPage(search);\n" +
                "        OperateStatus status = OperateStatus.defaultSuccess();\n" +
                "        status.setData(pageResult.getRows());\n" +
                "        return status;\n" +
                "    }");
        System.out.println();
        System.out.println("    @ResponseBody\n" +
                "    @RequestMapping(value = \"save\", method = RequestMethod.POST)\n" +
                "    @ApiOperation(value = \"新增|修改\", notes = \"NOTES\")\n" +
                "    public OperateStatus save(" + entityClass + " " + entityRef + ") {\n" +
                "        " + facadeRef + ".save(" + entityRef + ");\n" +
                "        return OperateStatus.defaultSuccess();\n" +
                "    }");
        System.out.println();
        System.out.println("    @ResponseBody\n" +
                "    @RequestMapping(value = \"delete\", method = RequestMethod.POST)\n" +
                "    @ApiOperation(value = \"删除\", notes = \"NOTES\")\n" +
                "    public OperateStatus delete(String id) {\n" +
                "        " + facadeRef + ".delete(id);\n" +
                "        return OperateStatus.defaultSuccess();\n" +
                "    }");
    }

    /**
     * String tableName = "position_feature_role";
     * String columnName = "feature_role_id";
     * boolean unique = false;
     *
     * @param columnName
     * @param unique
     */
    public static void buildIndexAnnotationStringForJPAEntity(String columnName, boolean unique) {
        System.out.println(", indexes = {");
        System.out.println("@Index(name = \"" +
                columnName.toUpperCase() +
                "_INDEX\", columnList = \"" +
                columnName +
                "\"" + (unique ? ", unique = true" : "") + ")");
        System.out.println("})");
    }

    /**
     * 1	LOWER_HYPHEN	连字符的变量命名规范如lower-hyphen
     * 2	LOWER_UNDERSCORE	c++变量命名规范如lower_underscore
     * 3	LOWER_CAMEL	java变量命名规范如lowerCamel
     * 4	UPPER_CAMEL	java和c++类的命名规范如UpperCamel
     * 5	UPPER_UNDERSCORE	java和c++常量的命名规范如UPPER_UNDERSCORE
     * <p>
     * 1.单独类名驼峰改为'@Entity public XXX extends BaseEntity{'的格式
     * 2.属性驼峰改为@Column(name="xxx",nullable=false,columnDefinition="varchar(100) COMMENT '字段描述'")
     * 其中，Date(以Date做后缀)/Enum(以enum做前缀)/Double(以Ratio或者Price做后缀)会单独处理
     */
    public static void parseEntityPropertyToColumnWithColumnDefinition() {
        String string = "systemUserId;name;browserEdition;osEdition;loginIpAddress;enumoperationType;operationReturnCode";
        String[] adjustNames = string.split(";");
        if (adjustNames.length == 1) {
            System.out.println("@Entity\n" +
                    "@Table(name = \"" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, adjustNames[0]) + "\")" +
                    "\n" +
                    "public class " + adjustNames[0] + " extends BaseEntity {");
            return;
        }
        for (int i = 0; i < adjustNames.length; i++) {
            if (adjustNames[i].startsWith("enum")) {
                System.out.println("@Enumerated(EnumType.STRING)" +
                        "\r\n@Column(name = \"" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, adjustNames[i].substring(4)) + "\", nullable = false, columnDefinition = \"varchar(100) COMMENT '字段描述'\")");
            } else if (adjustNames[i].equals("systemUserId")) {
                System.out.println("@Column(name = \"system_user_id\", nullable = false, columnDefinition = \"varchar(50) COMMENT '系统用户ID-关联system_user表'\")");
            } else if (adjustNames[i].endsWith("Date")) {
                System.out.println("@Column(name = \"" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, adjustNames[i]) + "\",  columnDefinition = \"datetime(0) COMMENT '日期描述'\")");
            } else if (adjustNames[i].endsWith("Ratio") || adjustNames[i].endsWith("Price")) {
                System.out.println("@Column(name = \"" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, adjustNames[i]) + "\", nullable = false, columnDefinition = \"double(10,2) COMMENT '费用说明'\")");
            } else {
                System.out.println("@Column(name = \"" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, adjustNames[i]) + "\", nullable = false, columnDefinition = \"varchar(100) COMMENT '字段描述'\")");
            }
            System.out.println("private");
            System.out.println();
        }
    }

    /**
     * Entity中将格式
     * <p>
     * '@Column(columnDefinition="varchar(100) COMMENT '字段注释'")'
     * <p>
     * 改为：
     * <p>
     * '/** *字段注释 * /'
     * '@Column(length=100)'
     * <p>
     * 的格式
     */
    public static void parseEntityPropertyToColumnWithoutColumnDefinition() {
        String string = "";
        int tempCombinedColumn = 0;
        String[] spiltString = string.split("@");
        for (int i = 1; i < spiltString.length; i++) {
            if (spiltString[i].startsWith("Enumerated")) {
                tempCombinedColumn = i + 1;
                System.out.println(transferCommentsAndLength(spiltString[i], spiltString[i + 1]));
                continue;
            }
            if (tempCombinedColumn != 0) {
                tempCombinedColumn = 0;
                continue;
            }
            System.out.println(transferCommentsAndLength(null, spiltString[i]));
        }
    }

    private static String transferCommentsAndLength(String enumDefine, String originalString1) {
        String originalString = originalString1;
        String[] columnDefinitions = originalString.split("columnDefinition");
        String[] lengthWithComments = columnDefinitions[1].split("\"\\)");
        String[] commentAndLength = lengthWithComments[0].split("COMMENT");
        String commentString = commentAndLength[1].substring(2, commentAndLength[1].length() - 1);
        String comment = "/**\n* " + commentString + "\n*/";
        String lengthOriginal = commentAndLength[0].substring(0, commentAndLength[0].length() - 2);
        String[] lengthOri = lengthOriginal.split("\\(");
        String lengthString = lengthOri[1].equals("0") ? "" : ",length=" + lengthOri[1];
        if (enumDefine != null) {
            String string = comment + "\n" + "@" + enumDefine + "@" + columnDefinitions[0].substring(0, columnDefinitions[0].lastIndexOf(",")) + lengthString + ")" + lengthWithComments[1];
            return string;
        } else {
            String string = comment + "\n" + "@" + columnDefinitions[0].substring(0, columnDefinitions[0].lastIndexOf(",")) + lengthString + ")" + lengthWithComments[1];
            return string;
        }
    }


}
