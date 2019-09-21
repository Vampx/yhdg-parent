package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalPlayLogMapper;
import cn.com.yusong.yhdg.agentserver.utils.AppUtils;
import cn.com.yusong.yhdg.common.domain.yms.TerminalPlayLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TerminalPlayLogService {
    @Autowired
    TerminalPlayLogMapper terminalPlayLogMapper;


    public TerminalPlayLog find(long id, String suffix) {
        return terminalPlayLogMapper.find(id, suffix);
    }


    public void tree(String dummy, OutputStream stream) throws IOException {
        Map<Integer, List<Integer>> yearMap = new HashMap<Integer, List<Integer>>();
        List<String> tableNameList = terminalPlayLogMapper.findTablelist();
        Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})");

        for(String name : tableNameList) {
            Matcher matcher = pattern.matcher(name);
            if(matcher.find()) {
                int year = Integer.parseInt(matcher.group(1)), month = Integer.parseInt(matcher.group(2));
                List<Integer> value = yearMap.get(year);
                if(value == null) {
                    value = new ArrayList<Integer>();
                    yearMap.put(year, value);
                }
                value.add(month);
            }
        }

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if(StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId(0);
            data.setName(dummy);
            roots.add(root);

            List<Integer> yearList = new ArrayList<Integer>(yearMap.keySet());
            Collections.sort(yearList);

            for (Integer year : yearList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> yearNode = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(String.format("year-%d", year));
                nodeModel.setName(String.valueOf(year));

                List<Integer> monthList = yearMap.get(year);
                Collections.sort(monthList);

                for (Integer month : monthList) {
                    NodeModel model = new NodeModel();
                    new Node<NodeModel>(model, yearNode);

                    model.setId(String.format("month-%d%02d", year, month));
                    model.setName(String.valueOf(month));
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);
        json.writeEndArray();

        json.flush();
        json.close();
    }

    public Page findPage(TerminalPlayLog search) {
        Page page = search.buildPage();
        page.setTotalItems(terminalPlayLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<TerminalPlayLog> list = terminalPlayLogMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    public int insert(TerminalPlayLog terminalAdLog) {
        return terminalPlayLogMapper.insert(terminalAdLog);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createTables() {
        Calendar calendar = DateUtils.truncate(Calendar.getInstance(), Calendar.MONTH);
        for(int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, i==0 ? 0: 1);
            String month = DateFormatUtils.format(calendar, "yyyyMM");
            if(StringUtils.isEmpty(terminalPlayLogMapper.tableExist(month))) {
                terminalPlayLogMapper.createTable(month);
                terminalPlayLogMapper.createFKAgentId(month);
                terminalPlayLogMapper.createFKTerminalId(month);
            }
        }
    }
}
