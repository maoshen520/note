package com.yun.note.util.vo;

import com.yun.note.pojo.FileDumpster;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class DeleteFileVo implements Serializable {
    @NotNull(message = "参数异常")
    private  Boolean complete;  //是否为彻底删除

    @NotNull(message = "参数异常")
    private Boolean dumpster; //是否为回收站操作

    @NotNull(message = "参数异常")
    @Size(min = 1)
    private List<FileDumpster> files;  //删除文件集合
}
