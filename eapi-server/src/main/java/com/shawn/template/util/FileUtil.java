package com.shawn.template.util;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import com.shawn.template.model.TreeNode;
/**
 * @ClassName FileTree
 * @Description TODO
 * @Author Shawn
 * @Date 2020/3/19 14:37
 * @Version 1.0
 **/
public class FileUtil {


    private final static Integer TYPE_FOLDER = 1; // 文件夹
    private final static Integer TYPE_FILE = 2; // 文件


    public static void main(String[] args) {

//        TreeNode node = readClassPathFileTree("meimeitechSpring");

//        System.out.println(JSON.toJSONString(node));
//        readClassPathFile("meimeitechSpring/libraries/spring-mvc/pom.mustache");

    }


//    public static void readClassPathFile(String name){
//
//        URL url = Resources.getResource(name);
//
//        List<String> lines;
//        try {
//            lines = Resources.asCharSource(url, Charsets.UTF_8).readLines();
//        } catch (IOException e) {
//            return;
//        }
//        System.out.println(JSON.toJSONString(lines));
//    }

    /**
     * 读取ClassPath下某文件夹下文件树
     *
     * @param name
     * @return
     */
    public static TreeNode readClassPathFileTree(String name) {

        final ClassPathResource classPathResource = new ClassPathResource(name);

        File folder;

        TreeNode node = null;

        try {
            folder = classPathResource.getFile();
        } catch (IOException e) {
            return node;
        }

        List<TreeNode> children = Lists.newArrayList();

        node = TreeNode.builder().title(folder.getName()).type(TYPE_FOLDER)
                .data(folder.getName()).build();

        toTree(folder, children, node);

        children.sort(Comparator.comparing(TreeNode::getType));

        return node;
    }

    private static void toTree(File file, List<TreeNode> children, TreeNode parent) {

        if (file.isFile()) {
            parent.setType(TYPE_FILE);
            return;
        }

        parent.setType(TYPE_FOLDER);
        parent.setChildren(children);

        String[] files = file.list();

        for (int i = 0; i < files.length; i++) {
            File f1 = new File(file.getPath() + File.separator + files[i]);

            TreeNode node = TreeNode.builder().title(f1.getName())
                    .data(parent.getData() + File.separator + f1.getName())
                    .build();

            children.add(node);

            List<TreeNode> objects = Lists.newArrayList();

            toTree(f1, objects, node);

            objects.sort(Comparator.comparing(TreeNode::getType));
        }
    }


}
