package com.administrator.learndemo.algorithm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.administrator.learndemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WideFirstActivity extends BaseAlgorithmActivity {

    private void startComputer() {
        //初始化先建立起各个节点信息，以及对应的直接子节点列表
        HashMap<String,String[]> map = new HashMap<>();
        map.put("A", new String[] {"B","C"});
        map.put("B", new String[] {"E"});
        map.put("C", new String[] {"D","F"});
        map.put("D", new String[] {"E"});
        map.put("E", new String[] {"H"});
        map.put("F", new String[] {"E","G"});
        map.put("G", new String[] {"H"});
        map.put("H", new String[] {});
        //获取从A到H的最短路径节点链表
        Node target = findTarget("A","H",map);
        //打印出最短路径的各个节点信息
        printSearPath(target);
    }


    /**
     * 打印出达到节点target所经过的各个节点信息
     * @param target
     */
    private void printSearPath(Node target) {
        if (target != null) {
            sb.append("找到了目标节点:" + target.id + "\n");

            List<Node> searchPath = new ArrayList<>();
            searchPath.add(target);
            Node node = target.parent;

            while (node != null) {
                searchPath.add(node);
                node = node.parent;
            }

            String path = "";

            for (int i = searchPath.size() - 1; i >= 0; i --) {
                path += searchPath.get(i).id;
                if(i!=0) {
                    path += "-->";
                }
            }

            sb.append("步数最短：" + path);
        } else {
            sb.append("未找到目标节点：");
        }

    }


    /**
     * 从指定的开始节点startId, 查询到目标节点tartId 的最短路径
     * @param startId
     * @param targetId
     * @param map
     * @return
     */
    private Node findTarget(String startId, String targetId, HashMap<String, String[]> map) {
        List<String> hasSearchList = new ArrayList<>();
        LinkedList<Node> queue = new LinkedList<>();

        queue.offer(new Node(startId, null));
        while (! queue.isEmpty()) {
            Node node = queue.poll();
            if (hasSearchList.contains(node.id)) {
                continue;
            }

            if (targetId.equals(node.id)) {
                return node;
            }

            hasSearchList.add(node.id);
            if (map.get(node.id) != null && map.get(node.id).length > 0) {
                for (String childId: map.get(node.id)) {
                    queue.offer(new Node(childId, node));
                }
            }
        }

        return null;
    }

    @Override
    protected void startCompile() {
        startComputer();
    }

    static class Node {
        public String id;
        public Node parent;
        public List<Node> childs = new ArrayList<>();

        public Node(String id, Node parent) {
            this.id = id;
            this.parent = parent;
        }
    }
}
