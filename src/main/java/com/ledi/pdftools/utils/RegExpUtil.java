package com.ledi.pdftools.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil implements java.io.Serializable {
    public static boolean isMatch(String content, String regExp) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(regExp)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    public static boolean hasMatch(String content, String regExp) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(regExp)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static String matcherText(String value, String regEx, int group) {
        if (StringUtils.isBlank(value) || StringUtils.isBlank(regEx)) {
            return null;
        }

        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(value);
        while (mat.find()) {
            return mat.group(group);
        }

        return null;
    }

    public static String getTextByReg(String content, String regExp, int group) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(regExp)) {
            return null;
        }

        Pattern pat = Pattern.compile(regExp);
        Matcher mat = pat.matcher(content);
        while (mat.find()) {
            return mat.group(group);
        }
        return null;
    }

    public static List<String> getTextsByReg(String content, String regExp, int group) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(regExp)) {
            return null;
        }

        Pattern pat = Pattern.compile(regExp);
        Matcher mat = pat.matcher(content);
        List<String> result = null;
        while (mat.find()) {
            if (result == null) {
                result = new ArrayList<String>();
            }
            result.add(mat.group(group));
        }
        return result;
    }

    public static String getMaxLengthText(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength);
    }

    public static void main(String[] args) {
        String html = "\n" +
                "\n" +
                "\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\" />\n" +
                "        <title>\n" +
                "\t通関状況検索\n" +
                "</title>\n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"jquery-easyui/themes/default/easyui.css\" />\n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"jquery-easyui/themes/icon.css\" />\n" +
                "        <script type=\"text/javascript\" src=\"jquery-easyui/jquery.min.js\"></script>\n" +
                "        <script type=\"text/javascript\" src=\"jquery-easyui/jquery.easyui.min.js\"></script>\n" +
                "        <script type=\"text/javascript\" src=\"jquery-easyui/locale/easyui-lang-zh_CN.js\"></script>\n" +
                "        <script type=\"text/javascript\" src=\"javascript/common.js\"></script>\n" +
                "        <script type=\"text/javascript\">\n" +
                "        $(function () {\n" +
                "            $.fn.extend({\n" +
                "                SimpleTree: function (options) {\n" +
                "\n" +
                "                    //初始化参数\n" +
                "                    var option = $.extend({\n" +
                "                        click: function (a) { }\n" +
                "                    }, options);\n" +
                "\n" +
                "                    option.tree = this;\t/* 在参数对象中添加对当前菜单树的引用，以便在对象中使用该菜单树 */\n" +
                "\n" +
                "                    option._init = function () {\n" +
                "                        /*\n" +
                "                         * 初始化菜单展开状态，以及分叉节点的样式\n" +
                "                         */\n" +
                "                        this.tree.find(\"ul ul\").hide();\t/* 隐藏所有子级菜单 */\n" +
                "                        this.tree.find(\"ul ul\").prev(\"li\").removeClass(\"open\");\t/* 移除所有子级菜单父节点的 open 样式 */\n" +
                "\n" +
                "                        this.tree.find(\"ul ul[show='true']\").show();\t/* 显示 show 属性为 true 的子级菜单 */\n" +
                "                        this.tree.find(\"ul ul[show='true']\").prev(\"li\").addClass(\"open\");\t/* 添加 show 属性为 true 的子级菜单父节点的 open 样式 */\n" +
                "                    }/* option._init() End */\n" +
                "\n" +
                "                    /* 设置所有超链接不响应单击事件 */\n" +
                "                    this.find(\"a\").click(function () { $(this).parents(\"li\").click(); return false; });\n" +
                "\n" +
                "                    /* 菜单项 \n" +
                "            <li> 接受单击 */\n" +
                "                    this.find(\"li\").click(function () {\n" +
                "                        /*\n" +
                "                         * 当单击菜单项 \n" +
                "                <li>\n" +
                "                         * 1.触发用户自定义的单击事件，将该 \n" +
                "                    <li> 标签中的第一个超链接做为参数传递过去\n" +
                "                         * 2.修改当前菜单项所属的子菜单的显示状态（如果等于 true 将其设置为 false，否则将其设置为 true）\n" +
                "                         * 3.重新初始化菜单\n" +
                "                         */\n" +
                "                        var a = $(this).find(\"a\")[0];\n" +
                "                        if (typeof (a) != \"undefined\")\n" +
                "                            option.click(a);\t/* 如果获取的超链接不是 undefined，则触发单击 */\n" +
                "\n" +
                "                        /* \n" +
                "                         * 如果当前节点下面包含子菜单，并且其 show 属性的值为 true，则修改其 show 属性为 false\n" +
                "                         * 否则修改其 show 属性为 true\n" +
                "                         */\n" +
                "                        if ($(this).next(\"ul\").attr(\"show\") == \"true\") {\n" +
                "                            $(this).next(\"ul\").attr(\"show\", \"false\");\n" +
                "                        } else {\n" +
                "                            $(this).next(\"ul\").attr(\"show\", \"true\");\n" +
                "                        }\n" +
                "\n" +
                "                        /* 初始化菜单 */\n" +
                "                        option._init();\n" +
                "                    });\n" +
                "\n" +
                "                    this.find(\"li\").hover(\n" +
                "                        function () {\n" +
                "                            $(this).addClass(\"hover\");\n" +
                "                        },\n" +
                "                        function () {\n" +
                "                            $(this).removeClass(\"hover\");\n" +
                "                        }\n" +
                "                    );\n" +
                "\n" +
                "                    /* 设置所有父节点样式 */\n" +
                "                    this.find(\"ul\").prev(\"li\").addClass(\"folder\");\n" +
                "\n" +
                "                    /* 设置节点“是否包含子节点”属性 */\n" +
                "                    this.find(\"li\").find(\"a\").attr(\"hasChild\", false);\n" +
                "                    this.find(\"ul\").prev(\"li\").find(\"a\").attr(\"hasChild\", true);\n" +
                "\n" +
                "                    /* 初始化菜单 */\n" +
                "                    option._init();\n" +
                "\n" +
                "                }/* SimpleTree Function End */\n" +
                "\n" +
                "            });\n" +
                "        });\n" +
                "    $(function(){\n" +
                "\t    //初始化树形菜单\n" +
                "\t    $(\".st_tree\").SimpleTree({\n" +
                "\t\t    click:function(a){\n" +
                "\t\t\t      if(!$(a).attr(\"hasChild\")) \n" +
                "\t\t\t      {\n" +
                "\t\t\t\t      var title=$(a).text();\n" +
                "\t\t\t\t      var url=$(a).attr(\"rel\");\n" +
                "\t\t\t\t      var icon=$(a).attr(\"icon\");\n" +
                "\t\t\t\t      parent.OpenTab(title,url,icon);\n" +
                "\t\t\t      }\n" +
                "\t\t    }\n" +
                "\t    });\t\n" +
                "    });\n" +
                "\n" +
                "    //$(function(){\n" +
                " \t//    /*为选项卡绑定右键*/\n" +
                "\t//    $(\".tabs li\").live('contextmenu',function(e){\n" +
                "\t\t\n" +
                "\t//\t    /* 选中当前触发事件的选项卡 */\n" +
                "\t//\t    var subtitle =$(this).text();\n" +
                "\t//\t    $('#tabs').tabs('select',subtitle);\n" +
                "\t\t\n" +
                "\t//\t    //显示快捷菜单\n" +
                "\t//\t    $('#menu').menu('show', {\n" +
                "\t//\t\t    left: e.pageX,\n" +
                "\t//\t\t    top: e.pageY\n" +
                "\t//\t    });\n" +
                "\t\t\n" +
                "\t//\t    return false;\n" +
                "\t//    });\n" +
                "    //});\n" +
                "\n" +
                "    \n" +
                "                    </script>\n" +
                "                    <script>\n" +
                "\t    function formatDownload(value, row) {\n" +
                "\t        if (row.PERMIT_DTIME) {\n" +
                "\t            return \"\n" +
                "                        <a href='/ashx/ReportFileManageOperate.ashx?token=\" + GlobalManager.Token + \"&operatetype=4&mawb=\" + row.MAWB_NO + \"&inv=\" + row.INVOICE_NO + \"' target='_blank'>DownLoad</a>\";\n" +
                "\t        } else {\n" +
                "\t            return \"\";\n" +
                "\t        }\n" +
                "\t    }\n" +
                "\n" +
                "\t    function formatthirdinvoice(value, row) {\n" +
                "\t        if (row.QUERYWEB) {\n" +
                "\t            return \"\n" +
                "                        <a href='\" + row.QUERYWEB + \"' target='_blank'>\" + row.THIRD_PART_INVOICENO + \"</a>\";\n" +
                "\t        } else {\n" +
                "\t            return \"\";\n" +
                "\t        }\n" +
                "\t    }\n" +
                "                    </script>\n" +
                "                    <script type=\"text/javascript\">\n" +
                "        formatterDate = function (date) {\n" +
                "            var day = date.getDate() > 9 ? date.getDate() : \"0\" + date.getDate();\n" +
                "            var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : \"0\"\n" +
                "            + (date.getMonth() + 1);\n" +
                "            return date.getFullYear() + '/' + month + '/' + day;\n" +
                "        };\n" +
                "\n" +
                "        $(document).ready(function () {\n" +
                "            var mm = new Date();\n" +
                "            var month = mm.getMonth();\n" +
                "            var curmonth = mm.getFullYear() + '/' + (month < 10 ? ('0' + month) : month);\n" +
                "\n" +
                "            //加载日期控件\n" +
                "            $('#txtDateFrom').datebox({\n" +
                "                width: '140px',\n" +
                "                editable: true,\n" +
                "                showSeconds:false,\n" +
                "                onChange: function (newValue, oldValue) {\n" +
                "                    var now = new Date();\n" +
                "\n" +
                "                    if (oldValue != \"\") {\n" +
                "                        if (newValue > $('#txtDateTo').datebox('getValue')) {\n" +
                "                            $('#txtDateFrom').datebox('setValue', oldValue);\n" +
                "                            MessageShow(171);\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            //datebox初始化赋值\n" +
                "            var yestday = getLocalTime(new Date().getTime() - 86400000)\n" +
                "            $('#txtDateFrom').datetimebox('setValue', yestday);\n" +
                "\n" +
                "            //加载日期控件\n" +
                "            $('#txtDateTo').datebox({\n" +
                "                width: '140px',\n" +
                "                editable: true,\n" +
                "                showSeconds: false,\n" +
                "                onChange: function (newValue, oldValue) {\n" +
                "                    var now = new Date();\n" +
                "                    if (oldValue != \"\") {\n" +
                "                        if (newValue < $('#txtDateFrom').datebox('getValue')) {\n" +
                "                            $('#txtDateTo').datebox('setValue', oldValue);\n" +
                "                            MessageShow(171);\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            //datebox初始化赋值\n" +
                "            $('#txtDateTo').datetimebox('setValue', getLocalTime(new Date().getTime()));\n" +
                "        });\n" +
                "\n" +
                "        function getLocalTime(nS) {\n" +
                "            var date = new Date(nS);\n" +
                "            var year = date.getFullYear();\n" +
                "            var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : \"0\"\n" +
                "            + (date.getMonth() + 1);\n" +
                "            var day = date.getDate() > 9 ? date.getDate() : \"0\" + date.getDate();\n" +
                "            return year + \"-\" + month + \"-\" + day + \" 00:00:00\";\n" +
                "        }\n" +
                " \n" +
                "        $(window).load(function () {\n" +
                "\n" +
                "            //网格初始化\n" +
                "            $('#grdMain').datagrid({\n" +
                "                iconCls: 'icon-search',\n" +
                "                //striped: true,\n" +
                "                //nowrap: true,\n" +
                "                rownumbers: false,\n" +
                "                remoteSort: false,\n" +
                "                fitColumns: false,\n" +
                "                frozenColumns: [[\n" +
                "                        { field: 'CUSTOMER_CD', title: '顧客番号', halign: 'center', frozen: true, sortable: false, width: 100, hidden: false },\n" +
                "                        { field: 'FLIGHT_DATE', title: '航班日期', halign: 'center', frozen: true, sortable: false, width: 80, hidden: false },\n" +
                "                        { field: 'MAWB_NO', title: 'MAWB_NO', halign: 'center', frozen: true, sortable: false, width: 100, hidden: false },\n" +
                "                        { field: 'INVOICE_NO', title: '送り状番号', halign: 'center', frozen: true, sortable: false, width: 100, hidden: false },\n" +
                "                        \n" +
                "                ]],\n" +
                "                columns: [[\n" +
                "                        { field: 'NULLTEST', title: '許可書DL', halign: 'center', frozen: true, align: 'center', halign: 'center', width: 100, formatter: formatDownload },\n" +
                "                        { field: 'TOTAL_NUMBER', title: '個数', align: 'right', halign: 'center', width: 50, sortable: false },\n" +
                "                        { field: 'TOTAL_WEIGHT', title: '重量', align: 'right', halign: 'center', width: 50, sortable: false },\n" +
                "                        { field: 'ENTRY_MOVEIN_DTIME', title: '蔵置場搬入日時', align: 'left', halign: 'center', width: 100, sortable: false },\n" +
                "                        { field: 'DECLARE_DTIME', title: '通関申告日時', align: 'left', halign: 'center', width: 100, sortable: false },\n" +
                "                        { field: 'PERMIT_DTIME', title: '許可日時', align: 'center', halign: 'center', width: 100, sortable: false },\n" +
                "                        { field: 'MISS_MEMO', title: '通关状态', align: 'left', halign: 'center', width: 220, sortable: false },\n" +
                "                        { field: 'DETAIN_REASON', title: '申告迟延理由', align: 'center', halign: 'center', width: 150, sortable: false },\n" +
                "                        { field: 'ENTRUST_DTIME', title: '転送日', align: 'center', halign: 'center', width: 100, sortable: false },\n" +
                "                        { field: 'ENTRUST_COMPANY_NAME', title: '業者', align: 'center', halign: 'center', width: 100, sortable: false },\n" +
                "                        { field: 'THIRD_PART_INVOICENO', title: '転送番号', align: 'center', halign: 'center', width: 350, formatter:formatthirdinvoice,sortable: false }\n" +
                "\n" +
                "                ]],\n" +
                "                pagination: true,\n" +
                "                pageSize: 15,\n" +
                "                pageList: [15, 20, 30, 50],\n" +
                "                pageNumber: 1,\n" +
                "                loadFilter: pagerFilter,\n" +
                "                pagePosition: 'bottom',\n" +
                "                toolbar: '#tb',\n" +
                "            });\n" +
                "\n" +
                "            ////获取页面分页对象\n" +
                "            //var p = $('#grdMain').datagrid('getPager');\n" +
                "            //if (p) {\n" +
                "            //    $(p).pagination({ //设置分页功能栏\n" +
                "            //        //分页功能可以通过Pagination的事件调用后台分页功能来实现\n" +
                "            //        onSelectPage: function (pageNumber, pageSize) {\n" +
                "            //            // \n" +
                "            //            SearchDataProcess();\n" +
                "            //        }\n" +
                "            //    })\n" +
                "            //}\n" +
                "\n" +
                "            //初始数据加载\n" +
                "            //SearchDataProcess();\n" +
                "        });\n" +
                "\n" +
                "\n" +
                "        function pagerFilter(data) {\n" +
                "\n" +
                "            if (typeof data.length == 'number' && typeof data.splice == 'function') {    // 判断数据是否是数组\n" +
                "\n" +
                "                data = {\n" +
                "\n" +
                "                    total: data.length,\n" +
                "\n" +
                "                    rows: data\n" +
                "\n" +
                "                }\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            var dg = $('#grdMain');\n" +
                "\n" +
                "            var opts = dg.datagrid('options');\n" +
                "\n" +
                "            var pager = dg.datagrid('getPager');\n" +
                "\n" +
                "            pager.pagination({\n" +
                "\n" +
                "                onSelectPage: function (pageNum, pageSize) {\n" +
                "\n" +
                "                    opts.pageNumber = pageNum;\n" +
                "\n" +
                "                    opts.pageSize = pageSize;\n" +
                "\n" +
                "                    pager.pagination('refresh', {\n" +
                "\n" +
                "                        pageNumber: pageNum,\n" +
                "\n" +
                "                        pageSize: pageSize\n" +
                "\n" +
                "                    });\n" +
                "\n" +
                "                    dg.datagrid('loadData', data);\n" +
                "\n" +
                "                }\n" +
                "\n" +
                "            });\n" +
                "\n" +
                "            if (!data.originalRows) {\n" +
                "\n" +
                "                data.originalRows = (data.rows);\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);\n" +
                "\n" +
                "            var end = start + parseInt(opts.pageSize);\n" +
                "\n" +
                "            data.rows = (data.originalRows.slice(start, end));\n" +
                "\n" +
                "            return data;\n" +
                "\n" +
                "        }\n" +
                "        //根据指定条件检索数据在网格中表示\n" +
                "        function SearchDataProcess(par, pageNumber, pageSize) {\n" +
                "            //debugger;\n" +
                "            var options = $('#grdMain').datagrid('getPager').data(\"pagination\").options;\n" +
                "            var url = \"/ashx/ReportFileManageOperate.ashx\";\n" +
                "            var queryParams = new Object();\n" +
                "            queryParams.operatetype = 0;\n" +
                "            queryParams.page = options.pageNumber == 0 ? 1 : options.pageNumber;\n" +
                "            queryParams.rows = options.pageSize;\n" +
                "            queryParams.token = GlobalManager.Token;\n" +
                "            queryParams.mawbno = $('input[id=\"txtMawb\"]').val();\n" +
                "            queryParams.invoiceno = $('input[id=\"txtInvoice\"]').val();\n" +
                "            queryParams.DateFrom = $(\"#txtDateFrom\").datebox(\"getValue\");\n" +
                "            queryParams.DateTo = $(\"#txtDateTo\").datebox(\"getValue\");\n" +
                "            if ($(\"#rdoTrans\").get(0).checked) {\n" +
                "                queryParams.DateKbn = 1;\n" +
                "            }\n" +
                "            else\n" +
                "            {\n" +
                "                queryParams.DateKbn = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotDeclare\").get(0).checked == true)\n" +
                "            {\n" +
                "                queryParams.DeclareFlag = 1;\n" +
                "            }\n" +
                "            else\n" +
                "            {\n" +
                "                queryParams.DeclareFlag = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotPermit\").get(0).checked == true) {\n" +
                "                queryParams.PermitFlag = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParams.PermitFlag = 0;\n" +
                "            }\n" +
                "            $.getJSON(url, queryParams, function (data) {\n" +
                "                if (data.StatusCode == 0)\n" +
                "                {\n" +
                "                    $(\"#grdMain\").datagrid(\"loadData\", data);\n" +
                "                }\n" +
                "                else\n" +
                "                {\n" +
                "                    ResultStateProcess(data.StatusCode);\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "        }\n" +
                "        function changedatelabel()\n" +
                "        {\n" +
                "            if ($(\"#rdoTrans\").get(0).checked)\n" +
                "            {\n" +
                "                $(\"#labeldate\")[0].innerHTML = \"转运日时\";\n" +
                "            }\n" +
                "            else\n" +
                "            {\n" +
                "                $(\"#labeldate\")[0].innerHTML = \"许可日时\";\n" +
                "            }\n" +
                "        }\n" +
                "        function DownloadMawbProcess() {\n" +
                "            var queryParamsDateKbn;\n" +
                "            var queryParamsDeclareFlag;\n" +
                "            var queryParamsPermitFlag;\n" +
                "            if ($(\"#rdoTrans\").get(0).checked) {\n" +
                "                queryParamsDateKbn = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsDateKbn = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotDeclare\").get(0).checked == true) {\n" +
                "                queryParamsDeclareFlag = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsDeclareFlag = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotPermit\").get(0).checked == true) {\n" +
                "                queryParamsPermitFlag = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsPermitFlag = 0;\n" +
                "            }\n" +
                "\n" +
                "            var url = '/ashx/ReportFileManageOperate.ashx?operatetype=2';\n" +
                "            url += \"&token=\" + GlobalManager.Token;\n" +
                "            url += \"&mawbno=\" + $('input[id=\"txtMawb\"]').val();\n" +
                "            url += \"&DateFrom=\" + $(\"#txtDateFrom\").datebox(\"getValue\");\n" +
                "            url += \"&DateTo=\" + $(\"#txtDateTo\").datebox(\"getValue\");\n" +
                "            url += \"&DateKbn=\" + queryParamsDateKbn;\n" +
                "            url += \"&DeclareFlag=\" + queryParamsDeclareFlag;\n" +
                "            url += \"&PermitFlag=\" + queryParamsPermitFlag;\n" +
                "            document.getElementById(\"hidbutton2\").href = url;\n" +
                "            document.getElementById(\"hidbutton2\").click();\n" +
                "        }\n" +
                "\n" +
                "        function DownloadEDIProcess() {\n" +
                "            var queryParamsDateKbn;\n" +
                "            var queryParamsDeclareFlag;\n" +
                "            var queryParamsPermitFlag;\n" +
                "            if ($(\"#rdoTrans\").get(0).checked) {\n" +
                "                queryParamsDateKbn = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsDateKbn = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotDeclare\").get(0).checked == true) {\n" +
                "                queryParamsDeclareFlag = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsDeclareFlag = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotPermit\").get(0).checked == true) {\n" +
                "                queryParamsPermitFlag = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsPermitFlag = 0;\n" +
                "            }\n" +
                "\n" +
                "            var url = '/ashx/ReportFileManageOperate.ashx?operatetype=3';\n" +
                "            url += \"&token=\" + GlobalManager.Token;\n" +
                "            url += \"&mawbno=\" + $('input[id=\"txtMawb\"]').val();\n" +
                "            url += \"&DateFrom=\" + $(\"#txtDateFrom\").datebox(\"getValue\");\n" +
                "            url += \"&DateTo=\" + $(\"#txtDateTo\").datebox(\"getValue\");\n" +
                "            url += \"&DateKbn=\" + queryParamsDateKbn;\n" +
                "            url += \"&DeclareFlag=\" + queryParamsDeclareFlag;\n" +
                "            url += \"&PermitFlag=\" + queryParamsPermitFlag;\n" +
                "            document.getElementById(\"hidbutton3\").href = url;\n" +
                "            document.getElementById(\"hidbutton3\").click();\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "         function DownloadCsvProcess() {\n" +
                "            var queryParamsDateKbn;\n" +
                "            var queryParamsDeclareFlag;\n" +
                "            var queryParamsPermitFlag;\n" +
                "            if ($(\"#rdoTrans\").get(0).checked) {\n" +
                "                queryParamsDateKbn = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsDateKbn = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotDeclare\").get(0).checked == true) {\n" +
                "                queryParamsDeclareFlag = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsDeclareFlag = 0;\n" +
                "            }\n" +
                "            if ($(\"#AllNotPermit\").get(0).checked == true) {\n" +
                "                queryParamsPermitFlag = 1;\n" +
                "            }\n" +
                "            else {\n" +
                "                queryParamsPermitFlag = 0;\n" +
                "            }\n" +
                "\n" +
                "            var url = '/ashx/ReportFileManageOperate.ashx?operatetype=1';\n" +
                "            url += \"&token=\" + GlobalManager.Token;\n" +
                "            url += \"&mawbno=\" + $('input[id=\"txtMawb\"]').val();\n" +
                "            url += \"&invoiceno=\" + $('input[id=\"txtInvoice\"]').val();\n" +
                "            url += \"&DateFrom=\" + $(\"#txtDateFrom\").datebox(\"getValue\");\n" +
                "            url += \"&DateTo=\" + $(\"#txtDateTo\").datebox(\"getValue\");\n" +
                "            url += \"&DateKbn=\" + queryParamsDateKbn;\n" +
                "            url += \"&DeclareFlag=\" + queryParamsDeclareFlag;\n" +
                "            url += \"&PermitFlag=\" + queryParamsPermitFlag;\n" +
                "            document.getElementById(\"hidbutton\").href = url;\n" +
                "            document.getElementById(\"hidbutton\").click();\n" +
                "        }\n" +
                "\n" +
                "        //页面上元素的有效性验证\n" +
                "        function CheckProcessLoop()\n" +
                "        {\n" +
                "            if (IsNullCheck($(\"#uploadfile\")[0].value, function (r) { $(\"#uploadfile\")[0].focus(); }) == false) {\n" +
                "                return false;\n" +
                "            }\n" +
                "            else\n" +
                "            {\n" +
                "                //限制文件类型\n" +
                "                var pos =  $(\"#uploadfile\")[0].value.replace(/.+\\./, \"\");\n" +
                "                if (pos.toUpperCase() != \"PDF\") {\n" +
                "                    MessageShow(209, function (r) { $(\"#uploadfile\")[0].focus(); })\n" +
                "                    return false;\n" +
                "                }\n" +
                "\n" +
                "                var size = $(\"#uploadfile\")[0].files[0].size / 1024;\n" +
                "                if (size > 1024*5) {\n" +
                "                    MessageShow(210, function (r) { $(\"#uploadfile\")[0].focus(); })\n" +
                "                    return false;\n" +
                "                }\n" +
                "\n" +
                "            }\n" +
                "            return true;\n" +
                "        }\n" +
                " \n" +
                "    \n" +
                "                    </script>\n" +
                "                </head>\n" +
                "                <body class=\"easyui-layout\"  overflow: auto;\" >\n" +
                "                    <div region=\"north\" style=\"height:80px; vertical-align:central\">\n" +
                "                        <!-- 页面头部 -->\n" +
                "                        <table style=\"width: 100%;\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"width: 278px; padding-top: 2px;\">\n" +
                "                        \n" +
                "                    </td>\n" +
                "                                <td style=\"padding-left: 10px;\">\n" +
                "                                    <h1 style=\"text-align:center;padding-top:10px\">WEB通関管理システム</h1>\n" +
                "                                </td>\n" +
                "                                <td style=\"width: 200px; padding-left: 10px;\">\n" +
                "                                    <div class=\"tel\"></div>\n" +
                "                                    <div class=\"tel\">\n" +
                "                                        <span style=\"width: 40px; margin-top: 10px;\">\n" +
                "                                            <img src=\"/images/userpic.png\" alt=\"\" />\n" +
                "                                        </span>\n" +
                "                                        <span style=\"margin-top: 10px; width: 90px; display: inline-block\" >\n" +
                "                                            <span id=\"lblUserName\" title=\"PRI  B2B\">\n" +
                "                                                <span style=\"margin - top: 10px; cursor: pointer;font-size:12px\" onclick=\"javascript: window.location.href = '#'\">PRI  B2B</span>\n" +
                "                                            </span>\n" +
                "                                        </span>\n" +
                "                                        <span style=\"width: 35px; margin: 5px; \">\n" +
                "                                            <img src=\"/images/quit.png\" />\n" +
                "                                        </span>\n" +
                "                                        <span style=\"margin - top: 10px; cursor: pointer\" onclick=\"javascript: window.location.href = '/Logout.aspx'\">退出</span>\n" +
                "                                    </div>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                    <form method=\"post\" action=\"./ShipList.aspx\" id=\"form2\">\n" +
                "                        <div class=\"aspNetHidden\">\n" +
                "                            <input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"F/lWfxC6kT+A1yyJ2LayYu/ADJJl4r9pCUyQ68ijfq1NKVm1IiG6irV83IUvs6QkX8qt5cyoy0icvQ19McFlxLw4PDL2vDBXRwtGcaq+MfQYTnbXkQKouAgsm+ywC49grtIxCIC762YgcTpKinhgx/XrN4y8NobSREJh40HELxx4ts3WcWGGdQcfJISsdIObdBvxDTd47sjR6ehIDWmwL/SPiHw6Q0Xgozqp2yQCjwFWeCDycsjvpSzq+wtwmHj+HpxkzeNgV8YCl4m4XcckW1KexREm2N79XaF8unCMbt4a8UwluqjOFGJEfXVTnkBPNnn8HOmu2mmcH7JjvrxJar1zUhPU8EwG+MQ0rOHc3IvFPEAwwr2YVbv0DNVkmAPIFa+QTgh3R9RIKe7C1Bmtce2SZtURya9gE/2ABUH+F9bhc3X/bk4QcywgcMlS4m8kEHf+6fVfHHDbxEWHJHE+aJNYRIFIbY0/puKPK+RfumPOR+KXQJpqhHJwkS4tz1rA9eW9Cjt6KfY3YF9MYCgR/rzHyQJak0YMEsBMOmoZ7evwNMFGdH3hiEKtcgh3/cAuyKW/7TiRVp2yCwqOYWx08t4FTegFSLcJNV7OOHKyWCSreL70pAuimRMivOB5GQPI\" />\n" +
                "                        </div>\n" +
                "                        <script>GlobalManager.Pkid='';GlobalManager.Token='e15edcae-7313-496c-94f1-b7746d64c774';</script>\n" +
                "                        <div class=\"aspNetHidden\">\n" +
                "                            <input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"CEEA3361\" />\n" +
                "                        </div>\n" +
                "                        <div class=\"easyui-panel\" region=\"west\" split=\"true\" style=\"width:220px;\" title=\"メニュ一\">\n" +
                "                            <ul class=\"easyui-tree\">\n" +
                "                                <li>\n" +
                "                                    <span>WEB通関管理システム</span>\n" +
                "                                    <ul>\n" +
                "                                        <li data-options=\"state:'closed'\">\n" +
                "                                            <span>通関管理</span>\n" +
                "                                            <ul>\n" +
                "                                                <li>\n" +
                "                                                    <a onclick=\"javascript: window.location.href = '/ShipList.aspx'\">\n" +
                "                                                        <span>通関状況検索</span>\n" +
                "                                                    </a>\n" +
                "                                                </li>\n" +
                "                                            </ul>\n" +
                "                                        </li>\n" +
                "                                    </ul>\n" +
                "                                </li>\n" +
                "                            </ul>\n" +
                "                        </div>\n" +
                "                        <div region=\"center\">\n" +
                "                            <div class=\"easyui-layout\" style=\"width: 100%; height: 100%; \" id=\"pagemain\">\n" +
                "                                <div data-options=\"region:'north'\" style=\"height:90px;\">\n" +
                "                                    <div class=\"pageBar\" style=\"height:100%\">\n" +
                "                                        <table style=\"width:100%;border:0;height:100%\">\n" +
                "                                            <tr style=\"\">\n" +
                "                                                <td style =\"\">\n" +
                "                                                    <table border=\"0\" class=\"SearchTable\">\n" +
                "                                                        <tr style=\"height:30px\">\n" +
                "                                                            <td id =\"labeldate\" style=\"width:70px;text-align:right\">许可日时</td>\n" +
                "                                                            <td style=\"text-align:left\">\n" +
                "                                                                <input style =\"width:130px;\" id=\"txtDateFrom\" type=\"text\" class=\"easyui-datetimebox\">\n" +
                "                                    \n" +
                "                                                            </td>\n" +
                "                                                            <td style =\"width:10px;text-align:center\">~</td>\n" +
                "                                                            <td style=\"text-align:left\">\n" +
                "                                                                <input style =\"width:150px;\" id=\"txtDateTo\" type=\"text\" class=\"easyui-datetimebox\">\n" +
                "                                    \n" +
                "                                                            </td>\n" +
                "                                                            <td colspan=\"3\">\n" +
                "                                                                <div>\n" +
                "                                                                    <input type=\"radio\" name=\"Gender\" id=\"rdoPermit\" value=\"1\" onchange=\"changedatelabel()\" checked=\"checked\" />根据许可时间查询\n" +
                "                                                                    <input type=\"radio\" name=\"Gender\" id=\"rdoTrans\" value=\"0\" onchange=\"changedatelabel()\" />根据转运时间查询\n" +
                "                                                                </div>\n" +
                "                                                            </td>\n" +
                "                                                            <td>\n" +
                "                                                                <input id=\"AllNotDeclare\" type=\"CheckBox\"/>所有未申告\n" +
                "                                                                <input id=\"AllNotPermit\" type=\"CheckBox\" />所有未许可\n" +
                "                                                            </td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr style=\"height:30px\">\n" +
                "                                                            <td style=\"width:70px;text-align:right\">MAWB番号</td>\n" +
                "                                                            <td colspan=\"3\">\n" +
                "                                                                <input type=\"text\" id=\"txtMawb\" class=\"textbox\" placeholder=\"\" style=\"width: 200px;height:24px\">\n" +
                "                                    \n" +
                "                                                            </td>\n" +
                "                                                            <td style=\"width:70px;text-align:right\">送り状番号</td>\n" +
                "                                                            <td>\n" +
                "                                                                <input type=\"text\" id=\"txtInvoice\" class=\"textbox\" placeholder=\"\" style=\"width: 200px;height:24px\">\n" +
                "                                    \n" +
                "                                                            </td>\n" +
                "                                                            <td>\n" +
                "                                                                <div style=\"padding: 5px 20px; text-align: right;vertical-align:top\">\n" +
                "                                                                    <a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-search'\"  onclick=\"SearchDataProcess()\">检 索</a>\n" +
                "                                                                </div>\n" +
                "                                                            </td>\n" +
                "                                                            <td>\n" +
                "                                                                <table>\n" +
                "                                                                    <tr>\n" +
                "                                                                        <td>\n" +
                "                                                                            <div style=\"padding: 5px 20px; text-align: right;vertical-align:top\">\n" +
                "                                                                                <a href=\"#\"  id =\"hidbutton\"  style =\"display:none;\" target =\"_blank\">tttt</a>\n" +
                "                                                                                <a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-save'\"  onclick=\"DownloadCsvProcess();\">CSV下载</a>\n" +
                "                                                                            </div>\n" +
                "                                                                        </td>\n" +
                "                                                                    </tr>\n" +
                "                                                                </table>\n" +
                "                                                            </td>\n" +
                "                                                        </tr>\n" +
                "                                                    </table>\n" +
                "                                                </td>\n" +
                "                                                <td style=\"\">\n" +
                "                            \n" +
                "                        </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </div>\n" +
                "                                </div>\n" +
                "                                <div data-options=\"region:'center'\">\n" +
                "                                    <table id=\"grdMain\" class=\"easyui-datagrid\" data-options=\"rownumbers:false,singleSelect:true\" style=\"width: 100%; height: 100%\">\n" +
                "\n" +
                "            </table>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </form>\n" +
                "                </body>\n" +
                "            </html>";


        html = html.replaceAll("\\t", "").replaceAll("\\n", "").replaceAll("\\r", "");
//        String reg1 = "<input(.*?)/>";
//        List<String> inputList = getTextsByReg(html, reg1, 1);
//        if (inputList != null) {
//            for (String str : inputList) {
//                System.out.println("content : " + str);
//
//                String regType = "type=\"(.*?)\"";
//                System.out.println("type : " + getTextByReg(str, regType, 1));
//                String regName = "name=\"(.*?)\"";
//                System.out.println("name : " + getTextByReg(str, regName, 1));
//                String regValue = "value=\"(.*?)\"";
//                System.out.println("value : " + getTextByReg(str, regValue, 1));
//                System.out.println("-----------");
//            }
//        }

        String regToken = "GlobalManager.Token='(.*?)'";
        System.out.println(getTextByReg(html, regToken, 1));
    }
}
