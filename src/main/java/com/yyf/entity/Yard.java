package com.yyf.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
@TableName("gm_yard")
public class Yard {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String yard;

    private Date yardTime;


}
