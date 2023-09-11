package org.example.tools.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Description:
 *
 * @author Song.Z
 */
@Data
public class SbomFileData {
    @ExcelProperty(value = "我方产品/组件名称", index = 0)
    private String productName = "org.example";
    @ExcelProperty(value = "子模块名称", index = 1)
    private String moduleName = "";
    @ExcelProperty(value = "引用第三方名称", index = 2)
    private String referenceName="";
    @ExcelProperty(value = "第三方许可证/授权合同", index = 3)
    private String license="";
    @ExcelProperty(value = "使用方式", index = 4)
    private String useMethod = "Java jar包引入";
    @ExcelProperty(value = "是否有改动(无修改使用 OR 修改/二开)", index = 5)
    private String changeSource = "无";
    @ExcelProperty(value = "功能简介", index = 6)
    private String description = "";
    @ExcelProperty(value = "代码托管网址", index = 7)
    private String codeUrl = "";
    @ExcelProperty(value = "产品官方网址", index = 8)
    private String webUrl = "";

    public static SbomFileData load(BomComponent bomComponent) {
        SbomFileData sbomFileData = new SbomFileData();
        sbomFileData.referenceName = String.format("%s v%s", bomComponent.getName(),
                Objects.requireNonNullElse(bomComponent.getVersion(), ""));

        StringBuilder licenseBuilder = new StringBuilder();
        for (License license : bomComponent.getLicenses()) {
            if (license != null && license.getLicense() != null && license.getLicense().getId() != null &&
                    !license.getLicense().getId().equals("") ) {
                if (licenseBuilder.length() > 0) {
                    licenseBuilder.append("; ");
                }
                licenseBuilder.append(license.getLicense().getId());
            }
        }
        sbomFileData.license = licenseBuilder.toString();
        sbomFileData.description = bomComponent.getDescription();
        for (ExternalReference externalReference : bomComponent.getExternalReferences()) {
            if (externalReference.getType().equals("website")) {
                sbomFileData.setWebUrl(externalReference.getUrl());
            }
            if (externalReference.getType().equals("vcs")) {
                sbomFileData.setCodeUrl(externalReference.getUrl());
            }
        }

        return sbomFileData;
    }
}
