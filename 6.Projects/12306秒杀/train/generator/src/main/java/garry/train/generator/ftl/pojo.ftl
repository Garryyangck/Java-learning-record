package garry.train.${module}.pojo;

import lombok.Data;
<#list typeSet as type>
<#if type=='Date'>
import java.util.Date;
</#if>
<#if type=='BigDecimal'>
import java.math.BigDecimal;
</#if>
</#list>

/**
 * @author Garry
 * ${DateTime}
 */
@Data
public class ${Domain} {

    <#list fieldList as field>
    /**
     * ${field.comment}
     */
    private ${field.javaType} ${field.nameHump};

    </#list>
}