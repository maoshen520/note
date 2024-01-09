    // import DecoupledEditor from '@ckeditor/ckeditor5-build-decoupled-document'  //官方打包好的--不可编辑
    // import {DecoupledEditor} from '@ckeditor/ckeditor5-editor-decoupled';  //可编辑的文档编辑器
import {DecoupledEditor} from '@ckeditor/ckeditor5-editor-decoupled';  //可编辑的文档编辑器
// import ParagraphPlugin from "@ckeditor/ckeditor5-paragraph/src/paragraph"
import {Paragraph} from '@ckeditor/ckeditor5-paragraph'
// import {Paragraph} from '@ckeditor/ckeditor5-paragraph';  //段落
import {Essentials} from '@ckeditor/ckeditor5-essentials';  //基础插件

// 编辑器的类型
export const editorType = DecoupledEditor;

// 获取编辑器的配置
export const getEditorConfigs = () => ({

    // 插件
    Plugins: [
        // ParagraphPlugin
        Paragraph,//段落插件
        Essentials,  //基础插件--输入功能、上一步、下一步等
    ],

    // 工具栏
    toolbar:{
        item:
        [
            'undo',  //撤销
            'redo',  //重做
            'selectAll',  //全选
        ]
    } 

})