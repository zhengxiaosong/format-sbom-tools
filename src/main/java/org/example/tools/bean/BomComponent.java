package org.example.tools.bean;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.Objects;

/**
 * Description:
 *
 * @author Song.Z
 */
@Data
public class BomComponent {
    private String publisher;
    private String group;
    private String name;
    private String version;
    private String description;
    private String scope;
    private String purl;
    private String type;
    @Alias("bom-ref")
    private String bomRef;
    private License[] licenses;
    private ExternalReference[] externalReferences;

    public boolean equals(BomComponent bomComponent) {
        return getNullString(this.publisher).equals(getNullString(bomComponent.getPublisher())) &&
                getNullString(this.group).equals(getNullString(bomComponent.getGroup())) &&
                getNullString(this.name).equals(getNullString(bomComponent.getName())) &&
                getNullString(this.version).equals(getNullString(bomComponent.getVersion()));
    }

    private String getNullString(String data) {
        return Objects.requireNonNullElse(data, "");
    }
}
