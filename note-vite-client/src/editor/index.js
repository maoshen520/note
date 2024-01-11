// import DecoupledEditor from '@ckeditor/ckeditor5-build-decoupled-document'  //官方打包好的--不可编辑
// import {DecoupledEditor} from '@ckeditor/ckeditor5-editor-decoupled';  //可编辑的文档编辑器

import {DecoupledEditor} from '@ckeditor/ckeditor5-editor-decoupled';
import {Paragraph} from '@ckeditor/ckeditor5-paragraph';

import {Essentials} from '@ckeditor/ckeditor5-essentials';
import 'ckeditor5/build/translations/zh-cn' // 中文包

import {
    Bold, // 加粗
    Italic, // 斜体
    Strikethrough, // 删除线
    Underline, // 下划线
    Subscript, // 下标
    Superscript, // 上标
    Code // 行内代码
} from '@ckeditor/ckeditor5-basic-styles'
import '@ckeditor/ckeditor5-basic-styles/build/translations/zh-cn' // 简体中文

import {BlockQuote} from  '@ckeditor/ckeditor5-block-quote' // 块引用插件
import '@ckeditor/ckeditor5-block-quote/build/translations/zh-cn' // 块引用中文包

import {FontFamily, FontSize, FontColor, FontBackgroundColor} from '@ckeditor/ckeditor5-font';  //字体功能插件
import '@ckeditor/ckeditor5-font/build/translations/zh-cn' // 字体中文语言包

import {Alignment} from '@ckeditor/ckeditor5-alignment' // 文本对齐插件
import '@ckeditor/ckeditor5-alignment/build/translations/zh-cn' // 文本对齐中文包

import {HorizontalLine} from '@ckeditor/ckeditor5-horizontal-line' // 水平线插件
import '@ckeditor/ckeditor5-horizontal-line/build/translations/zh-cn' // 水平线中文包

import {PageBreak} from '@ckeditor/ckeditor5-page-break' // 分页插件
import '@ckeditor/ckeditor5-page-break/build/translations/zh-cn' // 分页插件中文简体语言包

import {
    Heading, // 标题插件
    Title, // 文档标题插件
} from '@ckeditor/ckeditor5-heading'
import '@ckeditor/ckeditor5-heading/build/translations/zh-cn' // 标题插件中文简体语言包

import {
    TodoList, // 待办事项列表插件
    ListProperties, // 列表插件
} from '@ckeditor/ckeditor5-list'
import '@ckeditor/ckeditor5-list/build/translations/zh-cn' // 列表插件中文简体语言包

import {
    Indent, // 缩进插件（列表）
    IndentBlock, // 缩进插件（块：段落，标题）
} from '@ckeditor/ckeditor5-indent'
import '@ckeditor/ckeditor5-indent/build/translations/zh-cn' // 块缩进中文包

import '@ckeditor/ckeditor5-indent/build/translations/zh-cn' // 块缩进中文包
import {CodeBlock} from '@ckeditor/ckeditor5-code-block' // 代码块插件
import '@ckeditor/ckeditor5-code-block/build/translations/zh-cn' // 代码块插件中文简体他语言包

import {FindAndReplace} from '@ckeditor/ckeditor5-find-and-replace' // 查找替换插件
import '@ckeditor/ckeditor5-find-and-replace/build/translations/zh-cn' // 查找替换插件中文简体语言包

import { 
    SpecialCharacters, // 特殊字符插件
    SpecialCharactersEssentials // 特殊字符插件（箭头，数学，拉丁文，货币，文本
} from '@ckeditor/ckeditor5-special-characters';
import '@ckeditor/ckeditor5-special-characters/build/translations/zh-cn' // 特殊字符插件中文简体语言包

import {Link} from '@ckeditor/ckeditor5-link' // 链接插件
import '@ckeditor/ckeditor5-link/build/translations/zh-cn' // 连接插件中文简体语言包

import {
    Table, // 表格插件
    TableToolbar, // 表格工具栏插件
    TableProperties, // 表属性插件
    TableCellProperties, // 单元格属性插件
    TableColumnResize, // 调整表列宽度插件
    TableCaption // 表标题插件
} from '@ckeditor/ckeditor5-table'
import '@ckeditor/ckeditor5-table/build/translations/zh-cn' // 表格插件中文简体语言包

import {Autoformat} from '@ckeditor/ckeditor5-autoformat' // 自动格式转换插件

import {TextTransformation} from '@ckeditor/ckeditor5-typing' // 文本自动转换插件

import {RemoveFormat} from '@ckeditor/ckeditor5-remove-format'  // 删除格式插件
import '@ckeditor/ckeditor5-remove-format/build/translations/zh-cn' // 删除格式插件中文简体语言包


import {
  Image, // 图像插件
  ImageResize, // 调整图像尺寸插件
  ImageStyle, // 图像样式插件
  ImageToolbar, // 图像工具栏插件
  ImageTextAlternative, // 文本替换图像插件
  ImageCaption, // 图像标题插件
  ImageInsert, // 插入图像插件
  AutoImage // 自动图像插件
} from '@ckeditor/ckeditor5-image'
import {LinkImage} from '@ckeditor/ckeditor5-link' // 图像链接插件
import '@ckeditor/ckeditor5-image/build/translations/zh-cn' // 图像插件中文简体语言包
import {SimpleUploadAdapter} from '@ckeditor/ckeditor5-upload'  // 简单图像上传适配器插件

// 编辑器的类型
export const editorType = DecoupledEditor;

// 获取编辑器的配置
export const getEditorConfigs = () => ({
    language: 'zh-cn', // 中文包
    // 插件
    plugins: [
        Paragraph,  //段落插件
        Essentials,  //基础插件--输入、上一步、下一步等
        Bold, // 加粗
        Italic,  // 斜体
        Strikethrough, // 删除线
        Underline, // 下划线
        Superscript, // 上标
        Subscript, // 下标
        Code, // 行内代码
        BlockQuote, //块引用
        FontFamily, //字体
        FontSize, //字体大小
        FontColor, //字体颜色
        FontBackgroundColor, //字体背景色
        Alignment,  //文本对齐
        HorizontalLine, // 水平线插件
        PageBreak, // 分页插件
        Heading, // 标题插件
        Title, // 文档标题插件
        ListProperties, // 列表插件
        TodoList, // 待办事项列表插件
        Indent, // 缩进插件（列表）
        IndentBlock, // 缩进插件（块：段落，标题）
        CodeBlock, // 代码块插件
        FindAndReplace, // 查找替换插件
        SpecialCharacters, // 特殊字符插件
        SpecialCharactersEssentials, // 特殊字符插件（箭头，数学，拉丁文，货币，文本）
        Table, // 表格插件
        TableToolbar, // 表工具栏
        TableProperties, // 表属性插件
        TableCellProperties, // 单元格属性插件
        TableColumnResize, // 调整表列宽度插件
        TableCaption, // 表标题插件
        Link, // 链接插件
        Autoformat, // 自动格式转换插件
        // TextTransformation, // 文本自动转换插件
        RemoveFormat, // 删除格式插件
        Image, // 图像插件
        ImageResize, // 调整图像尺寸插件
        ImageStyle, // 图像样式插件
        ImageToolbar, // 图像工具栏插件
        ImageTextAlternative, // 文本替换图像插件
        ImageCaption, // 图像标题插件
        ImageInsert, // 插入图像插件
        AutoImage, // 自动图像插件
        LinkImage, // 图像链接插件
        SimpleUploadAdapter, // 简单上传适配器
    ],

    // 工具栏
    toolbar: [
        'undo', // 撤消
        'redo', // 重做
        'selectAll', // 全选
        'bold', // 加粗
        'italic', // 斜体
        'strikethrough', // 删除线
        'underline', // 下划线
        'superscript', // 上标
        'subscript', // 下标
        'code', // 行内代码
        'blockQuote', // 块引用
        'FontFamily',
        'FontSize',
        'FontColor',
        'FontBackgroundColor',
        // 'alignment', // 文本对齐
        'alignment:left', // 左对齐
        'alignment:center', // 水平居中对齐
        'alignment:right', // 右对齐
        'alignment:justify', // 两端对齐
        'horizontalLine', // 水平线
        'pageBreak', // 分页
        'heading', // 标题
        'bulletedList', // 无序列表
        'numberedList', // 有序列表
        'todolist', // 待办事项列表
        'outdent', // 减少缩进
        'indent', // 增加缩进
        'codeblock', // 代码块
        'findandreplace', // 查找替换
        'specialcharacters', // 特殊字符
        'insertTable', // 插入表格
        'link', // 链接
        'removeFormat', // 删除格式
        'uploadImage', // 上传图像
        'insertImage', // 插入图像
        'resizeImage', // 调整图像尺寸选项
        
    ],
    // 字体大小
    fontSize: {
        supportAllValues: true, //保留粘贴的字体大小值
        options: [
            9,
            'default',
            17,
        ]
    },

    // 字体颜色
    fontColor: {  
        columns: 10, // 颜色展示的列数
        documentColors: 6, // 文档中使用的颜色数量
        colors: [
            { color: '#000000', label: '黑色' },
            { color: '#262626', label: '深灰 3' },
            { color: '#585A5A', label: '深灰 2' },
            { color: '#8A8F8D', label: '深灰 1' },
            { color: '#D8DAD9', label: '灰色' },
            { color: '#E7E9E8', label: '浅灰 4' },
            { color: '#EFF0F0', label: '浅灰 3' },
            { color: '#F4F5F5', label: '浅灰 2' },
            { color: '#FAFAFA', label: '浅灰 1' },
            { color: '#FFFFFF', label: '白色' },

            { color: '#DF2A3F', label: '红色' },
            { color: '#ED740C', label: '橘橙' },
            { color: '#ECAA04', label: '金盏黄' },
            { color: '#FBDE28', label: '柠檬黄' },
            { color: '#74B602', label: '绿色' },
            { color: '#1DC0C9', label: '青色' },
            { color: '#117CEE', label: '浅蓝' },
            { color: '#2F4BDA', label: '蓝色' },
            { color: '#601BDE', label: '紫色' },
            { color: '#D22D8D', label: '玫红' },

            { color: '#FBE4E7', label: '红色 1' },
            { color: '#FDE6D3', label: '橘橙 1' },
            { color: '#F9EFCD', label: '金盏黄 1' },
            { color: '#FBF5CB', label: '柠檬黄 1' },
            { color: '#E8F7CF', label: '绿色 1' },
            { color: '#CEF5F7', label: '青色 1' },
            { color: '#D9EAFC', label: '浅蓝 1' },
            { color: '#D9DFFC', label: '蓝色 1' },
            { color: '#E6DCF9', label: '紫色 1' },
            { color: '#FBDFEF', label: '玫红 1' },

            { color: '#F1A2AB', label: '红色 2' },
            { color: '#F8B881', label: '橘橙 2' },
            { color: '#F5D480', label: '金盏黄 2' },
            { color: '#FCE75A', label: '柠檬黄 2' },
            { color: '#C1E77E', label: '绿色 2' },
            { color: '#81DFE4', label: '青色 2' },
            { color: '#81BBF8', label: '浅蓝 2' },
            { color: '#96A7FD', label: '蓝色 2' },
            { color: '#BA9BF2', label: '紫色 2' },
            { color: '#F297CC', label: '玫红 2' },

            { color: '#E4495B', label: '红色 3' },
            { color: '#F38F39', label: '橘橙 3' },
            { color: '#F3BB2F', label: '金盏黄 3' },
            { color: '#EDCE02', label: '柠檬黄 3' },
            { color: '#8CCF17', label: '绿色 3' },
            { color: '#01B2BC', label: '青色 3' },
            { color: '#2F8EF4', label: '浅蓝 3' },
            { color: '#4861E0', label: '蓝色 3' },
            { color: '#7E45E8', label: '紫色 3' },
            { color: '#E746A4', label: '玫红 3' },

            { color: '#AD1A2B', label: '红色 4' },
            { color: '#C75C00', label: '橘橙 4' },
            { color: '#C99103', label: '金盏黄 4' },
            { color: '#A58F04', label: '柠檬黄 4' },
            { color: '#5C8D07', label: '绿色 4' },
            { color: '#07787E', label: '青色 4' },
            { color: '#0C68CA', label: '浅蓝 4' },
            { color: '#213BC0', label: '蓝色 4' },
            { color: '#4C16B1', label: '紫色 4' },
            { color: '#AE146E', label: '玫红 4' },

            { color: '#70000D', label: '红色 5' },
            { color: '#663000', label: '橘橙 5' },
            { color: '#664900', label: '金盏黄 5' },
            { color: '#665800', label: '柠檬黄 5' },
            { color: '#2A4200', label: '绿色 5' },
            { color: '#004347', label: '青色 5' },
            { color: '#00346B', label: '浅蓝 5' },
            { color: '#101E60', label: '蓝色 5' },
            { color: '#270070', label: '紫色 5' },
            { color: '#5C0036', label: '玫红 5' }
        ],
    },

    // 字体背景颜色
    fontBackgroundColor: {  
        columns: 10, // 颜色展示的列数
        documentColors: 6, // 文档中使用的颜色数量
        colors: [
            { color: '#000000', label: '黑色' },
            { color: '#262626', label: '深灰 3' },
            { color: '#585A5A', label: '深灰 2' },
            { color: '#8A8F8D', label: '深灰 1' },
            { color: '#D8DAD9', label: '灰色' },
            { color: '#E7E9E8', label: '浅灰 4' },
            { color: '#EFF0F0', label: '浅灰 3' },
            { color: '#F4F5F5', label: '浅灰 2' },
            { color: '#FAFAFA', label: '浅灰 1' },
            { color: '#FFFFFF', label: '白色' },

            { color: '#DF2A3F', label: '红色' },
            { color: '#ED740C', label: '橘橙' },
            { color: '#ECAA04', label: '金盏黄' },
            { color: '#FBDE28', label: '柠檬黄' },
            { color: '#74B602', label: '绿色' },
            { color: '#1DC0C9', label: '青色' },
            { color: '#117CEE', label: '浅蓝' },
            { color: '#2F4BDA', label: '蓝色' },
            { color: '#601BDE', label: '紫色' },
            { color: '#D22D8D', label: '玫红' },

            { color: '#FBE4E7', label: '红色 1' },
            { color: '#FDE6D3', label: '橘橙 1' },
            { color: '#F9EFCD', label: '金盏黄 1' },
            { color: '#FBF5CB', label: '柠檬黄 1' },
            { color: '#E8F7CF', label: '绿色 1' },
            { color: '#CEF5F7', label: '青色 1' },
            { color: '#D9EAFC', label: '浅蓝 1' },
            { color: '#D9DFFC', label: '蓝色 1' },
            { color: '#E6DCF9', label: '紫色 1' },
            { color: '#FBDFEF', label: '玫红 1' },

            { color: '#F1A2AB', label: '红色 2' },
            { color: '#F8B881', label: '橘橙 2' },
            { color: '#F5D480', label: '金盏黄 2' },
            { color: '#FCE75A', label: '柠檬黄 2' },
            { color: '#C1E77E', label: '绿色 2' },
            { color: '#81DFE4', label: '青色 2' },
            { color: '#81BBF8', label: '浅蓝 2' },
            { color: '#96A7FD', label: '蓝色 2' },
            { color: '#BA9BF2', label: '紫色 2' },
            { color: '#F297CC', label: '玫红 2' },

            { color: '#E4495B', label: '红色 3' },
            { color: '#F38F39', label: '橘橙 3' },
            { color: '#F3BB2F', label: '金盏黄 3' },
            { color: '#EDCE02', label: '柠檬黄 3' },
            { color: '#8CCF17', label: '绿色 3' },
            { color: '#01B2BC', label: '青色 3' },
            { color: '#2F8EF4', label: '浅蓝 3' },
            { color: '#4861E0', label: '蓝色 3' },
            { color: '#7E45E8', label: '紫色 3' },
            { color: '#E746A4', label: '玫红 3' },

            { color: '#AD1A2B', label: '红色 4' },
            { color: '#C75C00', label: '橘橙 4' },
            { color: '#C99103', label: '金盏黄 4' },
            { color: '#A58F04', label: '柠檬黄 4' },
            { color: '#5C8D07', label: '绿色 4' },
            { color: '#07787E', label: '青色 4' },
            { color: '#0C68CA', label: '浅蓝 4' },
            { color: '#213BC0', label: '蓝色 4' },
            { color: '#4C16B1', label: '紫色 4' },
            { color: '#AE146E', label: '玫红 4' },

            { color: '#70000D', label: '红色 5' },
            { color: '#663000', label: '橘橙 5' },
            { color: '#664900', label: '金盏黄 5' },
            { color: '#665800', label: '柠檬黄 5' },
            { color: '#2A4200', label: '绿色 5' },
            { color: '#004347', label: '青色 5' },
            { color: '#00346B', label: '浅蓝 5' },
            { color: '#101E60', label: '蓝色 5' },
            { color: '#270070', label: '紫色 5' },
            { color: '#5C0036', label: '玫红 5' }
        ],
    },

    // 文本对齐
    // alignment: {
    //     options: [
    //         'left', // 左对齐
    //         'right', // 右对齐
    //         'center', // 水平居中对齐
    //     ]
    // },
    
    // 标题提示
    title: {
        placeholder: '请给笔记添加标题',
    },
    placeholder: '请在此处输入笔记内容',
    heading: {
        options: [
            { model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1' },
            { model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2' },
            { model: 'heading3', view: 'h3', title: 'Heading 3', class: 'ck-heading_heading3' },
            { model: 'heading4', view: 'h4', title: 'Heading 4', class: 'ck-heading_heading4' },
            { model: 'heading5', view: 'h5', title: 'Heading 5', class: 'ck-heading_heading5' },
            { model: 'heading6', view: 'h6', title: 'Heading 6', class: 'ck-heading_heading6' },
        ]
    },

    // 代码块
    codeBlock: {
        indentSequence: '  ', // 代码缩进字符，false：禁用代码缩进功能
        languages: [
            { language: 'html', label: 'HTML' }, // code 元素的 class 属性值为 language-html
            { language: 'javascript', label: 'JavaScript', class: 'js-code' }, // code 元素的 class 属性值为 js-code
        ]
    },

    // 特殊字符
    specialCharacters: {
        // 特殊字符类目的顺序
        order: [
            'Mathematical',
            'Emoji',
            'Text',
            'Currency',
            'Arrows',
            'Latin'
        ]
    },

    // 表格
    table: {
        // 新建表格默认 1 行 1 列 为表头
        defaultHeadings: { rows: 1, columns: 1 },
        // 表工具栏（气球）
        contentToolbar: [
            'toggleTableCaption', // 打开/关闭表标题
            '|',
            'tableColumn', // 表列设置
            'tableRow', // 表行设置
            'mergeTableCells', // 合并单元格
            '|',
            'tableProperties', // 表属性
            'tableCellProperties', // 单元格属性
        ],
        // 表属性配置
        tableProperties: {
            // 边框颜色
            borderColors: [
                { color: '#000000', label: '黑色' },
                { color: '#262626', label: '深灰' },
                { color: '#D8DAD9', label: '灰色' },
                { color: '#FFFFFF', label: '白色' },
                { color: '#DF2A3F', label: '红色' },
                { color: '#ED740C', label: '橘橙' },
                { color: '#ECAA04', label: '金盏黄' },
                { color: '#FBDE28', label: '柠檬黄' },
                { color: '#74B602', label: '绿色' },
                { color: '#1DC0C9', label: '青色' },
                { color: '#117CEE', label: '浅蓝' },
                { color: '#2F4BDA', label: '蓝色' },
                { color: '#601BDE', label: '紫色' },
                { color: '#D22D8D', label: '玫红' }
            ],
            // 背景颜色
            backgroundColors: [
                { color: '#000000', label: '黑色' },
                { color: '#262626', label: '深灰' },
                { color: '#D8DAD9', label: '灰色' },
                { color: '#FFFFFF', label: '白色' },
                { color: '#DF2A3F', label: '红色' },
                { color: '#ED740C', label: '橘橙' },
                { color: '#ECAA04', label: '金盏黄' },
                { color: '#FBDE28', label: '柠檬黄' },
                { color: '#74B602', label: '绿色' },
                { color: '#1DC0C9', label: '青色' },
                { color: '#117CEE', label: '浅蓝' },
                { color: '#2F4BDA', label: '蓝色' },
                { color: '#601BDE', label: '紫色' },
                { color: '#D22D8D', label: '玫红' }
            ],
        },
        // 单元格属性配置
        tableCellProperties: {
            // 边框颜色
            borderColors: [
                { color: '#000000', label: '黑色' },
                { color: '#262626', label: '深灰' },
                { color: '#D8DAD9', label: '灰色' },
                { color: '#FFFFFF', label: '白色' },
                { color: '#DF2A3F', label: '红色' },
                { color: '#ED740C', label: '橘橙' },
                { color: '#ECAA04', label: '金盏黄' },
                { color: '#FBDE28', label: '柠檬黄' },
                { color: '#74B602', label: '绿色' },
                { color: '#1DC0C9', label: '青色' },
                { color: '#117CEE', label: '浅蓝' },
                { color: '#2F4BDA', label: '蓝色' },
                { color: '#601BDE', label: '紫色' },
                { color: '#D22D8D', label: '玫红' }
            ],
            // 背景颜色
            backgroundColors: [
                { color: '#000000', label: '黑色' },
                { color: '#262626', label: '深灰' },
                { color: '#D8DAD9', label: '灰色' },
                { color: '#FFFFFF', label: '白色' },
                { color: '#DF2A3F', label: '红色' },
                { color: '#ED740C', label: '橘橙' },
                { color: '#ECAA04', label: '金盏黄' },
                { color: '#FBDE28', label: '柠檬黄' },
                { color: '#74B602', label: '绿色' },
                { color: '#1DC0C9', label: '青色' },
                { color: '#117CEE', label: '浅蓝' },
                { color: '#2F4BDA', label: '蓝色' },
                { color: '#601BDE', label: '紫色' },
                { color: '#D22D8D', label: '玫红' }
            ],
        }
    },

    // 超链接
    link: {
        // addTargetToExternalLinks: true, // 编辑器创建新的超链接自动添加 target='_blank' 和 rel 属性
        defaultProtocol: 'http://', // 默认的网络协议
        decorators: {
            // 是否已新标签页打开链接
            linkTarget: {
                mode: 'manual', // 手动模式
                label: '是否已新标签页打开链接？',
                defaultValue: true, // 开关已打开
                attributes: {
                    target: '_blank', // 开关打开状态下，给链接添加的属性值
                }
            },
            // 绿色的链接
            linkGreen: {
                mode: 'automatic', // 自动模式
                callback: url => {
                    // 判断链接地址是否是 https 网络协议
                    if(url.indexOf('https://') !== -1){
                        return true
                    }
                    return false
                },
                attributes: {
                    style: 'color: green', // 如果是 https 网络协议，将链接变成绿色
                }
            }
        }
    },

    // 文本自动转换插件
    // typing: {
    //     transformations: {
    //         // 添加自定义自动转换文本
    //         extra: [
    //             { from: 'CKE', to: 'CKEditor' }
    //         ],
    //         // 编辑器包含哪些自定义转换文本
    //         include: [
    //             'mathematical', // 包含数学方面的
    //             'copyright', // 包含  (c) ©
    //             { from: 'ff', to: '飞飞' } // 还可以在这自定义（尽量自定义放到 extra）
    //         ],
    //         // 移出自动转换
    //         remove: [
    //             'oneForth', // 只移出 1/4 ¼
    //             'quotes', // 移出一个组（引号）
    //         ]
    //     }
    // }

    // 图片
    image: {
        toolbar: [
            'link', // 图像链接
            'imageTextAlternative', // 文本替换图像
            'toggleImageCaption', // 显示或隐藏图像标题
            'resizeImage', // 调整图像尺寸选项
            '|',
            'resizeImage:original', // 原始尺寸
            'resizeImage:25', // 25%
            'resizeImage:50', // 50%
            'resizeImage:75', // 75%
            'imageStyle:inline', // 显示在文本行内
            'imageStyle:alignLeft', // 图像与文本包装在一起左对齐
            'imageStyle:alignRight', // 图像与文本包装在一起右对齐
            'imageStyle:side', // 图像侧边显示
            '|',
            'imageStyle:alignBlockLeft', // 块图像左对齐
            'imageStyle:alignBlockRight', // 块图像右对齐
            'imageStyle:alignCenter', // 图像居中对齐
            // 'imageStyle:block', // 块图像（居中）
        ]
    },

    // 简单上传适配器
    simpleUpload: {
        uploadUrl: '/note-serve/ckeditor/upload/pic', // 上传图像服务器地址（可能需要解决跨域问题）
        // withCredentials: true, // 启用 XMLHttpRequest.withCredentials 属性
        // headers: {
        //     'Accept-Api-Key': 'ASK_D_FF', // 请求头设置来验证身份的（自己定义属性）
        // }
    },

})