package org.example.tools;


import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import org.example.tools.bean.Bom;
import org.example.tools.bean.BomComponent;
import org.example.tools.bean.SbomFileData;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 * Description:
 *
 * @author Song.Z
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("Begin analysis!");

        String target = "/Users/song/Desktop/socialhub-bsom/";
        File path = new File(target);        //获取其file对象
        File[] fs = path.listFiles();    //遍历path下的文件和目录，放在File数组中
        if (fs == null) {
            System.out.printf("Can't find path [%s]\n", target);
            System.exit(0);
        }

        List<String> jsonFileList = retrieveAllJsonFile(fs);
        List<BomComponent> componentList = new LinkedList<>();
        int nonLicenseCount = 0;
        List<SbomFileData> sbomFileDataList = new LinkedList<>();
        for (String jsonFile : jsonFileList) {
            System.out.printf("%s\n", jsonFile);
            FileReader fileReader = new FileReader(jsonFile);
            String jsonString = fileReader.readString();
            Bom bom = JSONUtil.toBean(jsonString, Bom.class);
            if (bom == null || bom.getComponents() == null) {
                continue;
            }
            for (BomComponent component : bom.getComponents()) {
                if (component.getGroup().startsWith("org.example")) {
                    continue;
                }
                if (componentList.stream().noneMatch(c -> c.equals(component))) {
                    componentList.add(component);
                    SbomFileData sbomFileData = SbomFileData.load(component);
                    if (bom.getMetadata() != null && bom.getMetadata().getComponent() != null &&
                            bom.getMetadata().getComponent().getName() != null) {
                        sbomFileData.setModuleName(bom.getMetadata().getComponent().getName());
                    }
                    sbomFileDataList.add(sbomFileData);

                    if (component.getLicenses().length == 0 ||
                            component.getLicenses()[0].getLicense() == null ||
                            StringUtils.isEmpty(component.getLicenses()[0].getLicense().getId())
                            ) {
                        nonLicenseCount ++;
                    }
                }
            }
        }
        EasyExcel.write(target+"SBOM.xls", SbomFileData.class)
                .sheet("SBOM").doWrite(sbomFileDataList);

        System.out.printf("Analysis complete! Total count is %d, Non-License count is %d\n",
                componentList.size(), nonLicenseCount);
        System.out.printf("Target file: %s \n", target + "SBOM.xls");
    }


    static List<String> retrieveAllJsonFile(File[] files) {
        if (files == null) {
            return new LinkedList<>();
        }

        List<String> jsonFileList = new LinkedList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                List<String> tmpList = retrieveAllJsonFile(file.listFiles());
                jsonFileList.addAll(tmpList);
                continue;
            }

            if (file.isFile() && file.getName().endsWith(".json")) {
                jsonFileList.add(file.getPath());
            }
        }
        return jsonFileList;
    }
}