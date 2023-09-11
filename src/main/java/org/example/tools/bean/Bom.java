package org.example.tools.bean;

import lombok.Data;

/**
 * Description:
 *
 * @author Song.Z
 */
@Data
public class Bom {
    private String bomFormat;
    private String specVersion;
    private String serialNumber;
    private String version;
    private MetaData metadata;
    private BomComponent[] components;
}
