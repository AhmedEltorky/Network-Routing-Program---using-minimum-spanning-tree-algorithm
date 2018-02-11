package networkprojectrouting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author ENG-TORKY
 */
public class View extends javax.swing.JFrame {

    private final Graphics2D gfx;
    private final DisplayMode dm;
    private final Screen s;
    private final Image backGround, node, icon, deleteNode, exit, connect, reset, cursor, solve, save, load;
    private static final int NODE_WIDTH = 64, NODE_HEIGHT = 64, START_LINE = 0, END_LINE = 1;
    private static final int CREATE_NODE = 0, CONNECT_NODES = 1, DELETE_NODE = 2, DRAG_NODE = 3, LOAD = 4, SAVE = 5;
    private static int status = -1;
    private HashMap<Integer, Point> nodesLocation;
    private HashMap<Integer, Integer[]> lines;
    private HashMap<Integer, Point> valuesLocation;
    private HashMap<Integer, String> values;
    private int nodeID = 0, lineID = 0, dragNodeID = -1, deleteNodeID = -1;
    private int startNodeID = -1, endNodeID = -1;
    private boolean dragNode = false, startLineFlag = false, lineExist = false;
    private MinimumSpanningTree mst;

    /**
     * Creates new form View
     */
    public View() {
        initComponents();
        backGround = new ImageIcon("imgs/back.jpg").getImage();
        node = new ImageIcon("imgs/router.png").getImage();
        icon = new ImageIcon("imgs/icon.png").getImage();
        deleteNode = new ImageIcon("imgs/delete.png").getImage();
        exit = new ImageIcon("imgs/exit.png").getImage();
        connect = new ImageIcon("imgs/connect.png").getImage();
        reset = new ImageIcon("imgs/reset.png").getImage();
        cursor = new ImageIcon("imgs/cursor.png").getImage();
        solve = new ImageIcon("imgs/solve.png").getImage();
        save = new ImageIcon("imgs/save.png").getImage();
        load = new ImageIcon("imgs/load.png").getImage();
        s = new Screen();
        dm = new DisplayMode(1366, 768, 32, 60);
        s.setFullScreen(dm, this);
        s.setWindowTitle("Network Project Routing 1.0", this);
        s.setWindowIcon(icon, this);
        gfx = (Graphics2D) this.getGraphics();
        nodesLocation = new HashMap<>();
        lines = new HashMap<>();
        valuesLocation = new HashMap<>();
        values = new HashMap<>();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getY() <= 734 && evt.getY() >= 670) {
            if (evt.getX() >= 270 && evt.getX() <= 334) {
                this.collectData();
            } else if (evt.getX() >= 490 && evt.getX() <= 554) {
                status = LOAD;
                this.loadProject();
                this.paint(gfx);
            } else if (evt.getX() >= 710 && evt.getX() <= 774) {
                status = SAVE;
                this.saveProject();
                this.paint(gfx);
            }
        } else if (evt.getX() >= 1220 && evt.getX() <= 1284) {
            if (evt.getY() <= 124 && evt.getY() >= 60) {
                status = CREATE_NODE;
                this.paint(gfx);
            } else if (evt.getY() <= 224 && evt.getY() >= 160) {
                status = CONNECT_NODES;
                this.paint(gfx);
            } else if (evt.getY() <= 324 && evt.getY() >= 260) {
                status = DELETE_NODE;
                this.paint(gfx);
            } else if (evt.getY() <= 424 && evt.getY() >= 360) {
                status = DRAG_NODE;
                this.paint(gfx);
            } else if (evt.getY() <= 524 && evt.getY() >= 460) {
                this.resetProgram();
            } else if (evt.getY() <= 624 && evt.getY() >= 560) {
                s.restoreScreen();
            }
        } else {
            if (status == CREATE_NODE) {
                if (this.checkNotNode(evt.getX(), evt.getY())) {
                    nodesLocation.put(nodeID, new Point(evt.getX(), evt.getY()));
                    nodeID++;
                    this.drawAllNodes();
                }
            } else if (status == DELETE_NODE) {
                if (this.checkNodeForDelete(evt.getX(), evt.getY())) {
                    this.deleteNode(deleteNodeID);
                    deleteNodeID = -1;
                    this.paint(gfx);
                }
            }
        }
    }//GEN-LAST:event_formMouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (status == CONNECT_NODES) {
            if (this.checkNodeForLine(evt.getX(), evt.getY(), START_LINE)) {
                startLineFlag = true;
            }
        } else if (status == DRAG_NODE) {
            if (this.checkNodeForDrag(evt.getX(), evt.getY())) {
                dragNode = true;
            } else if (this.checkIfValue(evt.getX(), evt.getY())) {
                Point p = new Point(evt.getX(), evt.getY());
                int id = -1;
                for (int i = 0; i < valuesLocation.size(); i++) {
                    if (p.x >= valuesLocation.get(i).x - 10 && p.x <= valuesLocation.get(i).x + 10) {
                        if (p.y <= valuesLocation.get(i).y + 10 && p.y >= valuesLocation.get(i).y - 10) {
                            id = i;
                            break;
                        }
                    }
                }
                String newValue = JOptionPane.showInputDialog(null, "please enter the new value:", "Changing Line Value", JOptionPane.QUESTION_MESSAGE);
                if (this.checkValidityOfValue(newValue)) {
                    values.replace(id, newValue);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "You Entered Invalid Value !!!", "Invalid Value", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (dragNode == true) {
            for (int i = 0; i < nodesLocation.size(); i++) {
                if ((Integer) nodesLocation.keySet().toArray()[i] == dragNodeID) {
                    Point p = (Point) nodesLocation.values().toArray()[i];
                    p.x = evt.getX();
                    p.y = evt.getY();
                    break;
                }
            }
            this.paint(gfx);
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if (status == CONNECT_NODES && startLineFlag == true) {
            if (this.checkNodeForLine(evt.getX(), evt.getY(), END_LINE)) {
                if (startNodeID != endNodeID) {
                    Integer[] ns = {startNodeID, endNodeID};
                    // check if this line is exist
                    for (int i = 0; i < lines.size(); i++) {
                        Integer[] temp = (Integer[]) lines.values().toArray()[i];
                        if ((ns[0] == temp[0] && ns[1] == temp[1]) || (ns[0] == temp[1] && ns[1] == temp[0])) {
                            lineExist = true;
                            break;
                        }
                    }
                    if (!lineExist) {
                        lines.put(lineID, ns);
                        lineID++;
                        this.paint(gfx);
                    } else {
                        startNodeID = -1;
                        endNodeID = -1;
                        startLineFlag = false;
                        lineExist = false;
                    }
                } else {
                    startNodeID = -1;
                    endNodeID = -1;
                    startLineFlag = false;
                }
            } else {
                startNodeID = -1;
                endNodeID = -1;
                startLineFlag = false;
            }
        }
        //
        if (dragNode) {
            dragNodeID = -1;
            dragNode = false;
        }
    }//GEN-LAST:event_formMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new View().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void drawAllNodes() {
        for (int i = 0; i < nodesLocation.size(); i++) {
            Point p = (Point) nodesLocation.values().toArray()[i];
            gfx.drawImage(node, p.x - (NODE_WIDTH / 2), p.y - (NODE_HEIGHT / 2), rootPane);
            gfx.setFont(new Font("Arial", Font.BOLD, 14));
            gfx.setColor(Color.black);
            gfx.drawString("Node " + ((Integer) nodesLocation.keySet().toArray()[i] + 1), p.x - 20, p.y + 45);
        }
    }

    public void drawAllLines() {
        Point startNode = null, endNode = null;
        for (int i = 0; i < lines.size(); i++) {
            Integer startEndLine[] = (Integer[]) lines.values().toArray()[i];
            // check for start node of line
            for (int j = 0; j < nodesLocation.size(); j++) {
                if (startEndLine[0] == (Integer) nodesLocation.keySet().toArray()[j]) {
                    startNode = (Point) nodesLocation.values().toArray()[j];
                }
            }
            // check for end node of line
            for (int j = 0; j < nodesLocation.size(); j++) {
                if (startEndLine[1] == (Integer) nodesLocation.keySet().toArray()[j]) {
                    endNode = (Point) nodesLocation.values().toArray()[j];
                }
            }
            // draw the line
            gfx.setStroke(new BasicStroke(3));
            gfx.setColor(Color.gray);
            if (startNode != null && endNode != null) {
                gfx.drawLine(startNode.x, startNode.y, endNode.x, endNode.y);
            }
        }
    }

    public void drawAllValues() {
        Integer[] psID;
        int lineID;
        Point sp = null, ep = null;
        for (int i = 0; i < lines.size(); i++) {
            lineID = (Integer) lines.keySet().toArray()[i];
            psID = (Integer[]) lines.values().toArray()[i];
            for (int j = 0; j < nodesLocation.size(); j++) {
                if (psID[0] == (Integer) nodesLocation.keySet().toArray()[j]) {
                    sp = (Point) nodesLocation.values().toArray()[j];
                }
            }
            for (int j = 0; j < nodesLocation.size(); j++) {
                if (psID[1] == (Integer) nodesLocation.keySet().toArray()[j]) {
                    ep = (Point) nodesLocation.values().toArray()[j];
                }
            }
            if (sp != null && ep != null) {
                Point p = new Point((sp.x + ep.x) / 2, (sp.y + ep.y) / 2);
                valuesLocation.put(lineID, p);
                if (values.get(lineID) == null) {
                    values.put(lineID, "??");
                }
                gfx.setColor(Color.BLACK);
                gfx.drawString((String) values.get(lineID), p.x, p.y);
            }
        }
    }

    public boolean checkNotNode(int x, int y) {
        if (x >= 1130) {
            return false;
        }
        if (y >= 570) {
            return false;
        }
        for (int i = 0; i < nodesLocation.size(); i++) {
            Point checkPoint = (Point) nodesLocation.values().toArray()[i];
            if (x >= checkPoint.x - NODE_WIDTH && x <= checkPoint.x + NODE_WIDTH) {
                if (y <= checkPoint.y + NODE_HEIGHT && y >= checkPoint.y - NODE_HEIGHT) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkNodeForLine(int x, int y, int status) {
        for (int i = 0; i < nodesLocation.size(); i++) {
            Point checkPoint = (Point) nodesLocation.values().toArray()[i];
            if (x >= checkPoint.x - 20 && x <= checkPoint.x + 20) {
                if (y <= checkPoint.y + 20 && y >= checkPoint.y - 20) {
                    if (status == START_LINE) {
                        startNodeID = (Integer) nodesLocation.keySet().toArray()[i];
                    } else if (status == END_LINE) {
                        endNodeID = (Integer) nodesLocation.keySet().toArray()[i];
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkNodeForDrag(int x, int y) {
        for (int i = 0; i < nodesLocation.size(); i++) {
            Point checkPoint = (Point) nodesLocation.values().toArray()[i];
            if (x >= checkPoint.x - 20 && x <= checkPoint.x + 20) {
                if (y <= checkPoint.y + 20 && y >= checkPoint.y - 20) {
                    dragNodeID = (Integer) nodesLocation.keySet().toArray()[i];
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkNodeForDelete(int x, int y) {
        for (int i = 0; i < nodesLocation.size(); i++) {
            Point checkPoint = (Point) nodesLocation.values().toArray()[i];
            if (x >= checkPoint.x - 20 && x <= checkPoint.x + 20) {
                if (y <= checkPoint.y + 20 && y >= checkPoint.y - 20) {
                    deleteNodeID = (Integer) nodesLocation.keySet().toArray()[i];
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteNode(int node) {
        int maxLen = nodesLocation.size();
        for (int i = node; i < maxLen; i++) {
            if (i == node) {
                nodesLocation.remove(node);
                maxLen--;
            } else {
                nodesLocation.keySet().toArray()[i] = (Integer) nodesLocation.keySet().toArray()[i] - 1;
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            Integer lins[] = (Integer[]) lines.values().toArray()[i];
            if (lins[0] == node || lins[1] == node) {
                lines.remove(i, lins); // wrong here
                valuesLocation.remove(i);
                values.remove(i);
            }
        }
    }

    public boolean checkValidityOfValue(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean checkIfValue(int x, int y) {
        for (int i = 0; i < valuesLocation.size(); i++) {
            Point checkPoint = (Point) valuesLocation.values().toArray()[i];
            if (x >= checkPoint.x - 10 && x <= checkPoint.x + 10) {
                if (y <= checkPoint.y + 10 && y >= checkPoint.y - 10) {
                    return true;
                }
            }
        }
        return false;
    }

    public void collectData() {
        int[][] linesValues = new int[lines.size()][3];
        for (int i = 0; i < lines.size(); i++) {
            Integer[] nodesId = (Integer[]) lines.values().toArray()[i];
            linesValues[i][0] = nodesId[0];
            linesValues[i][1] = nodesId[1];
            linesValues[i][2] = Integer.parseInt(values.get(lines.keySet().toArray()[i]));
        }
        for (int[] linesValue : linesValues) {
            System.out.print(linesValue[0] + "\t" + linesValue[1] + "\t" + linesValue[2]);
            System.out.println();
        }
        int length = nodesLocation.size();
        mst = new MinimumSpanningTree(length);
        int arr[][] = new int[length][length];
        for (int i = 0; i < linesValues.length; i++) {
            arr[linesValues[i][0]][linesValues[i][1]] = linesValues[i][2];
            arr[linesValues[i][1]][linesValues[i][0]] = linesValues[i][2];
        }
        for (int r = 0; r < length; r++) {
            for (int c = 0; c < length; c++) {
                if (arr[r][c] == 0) {
                    arr[r][c] = 99;
                }
            }
        }
        for (int i = 0; i < length; i++) {
            arr[i][i] = 0;
        }
        System.out.println();
        for (int r = 0; r < length; r++) {
            for (int c = 0; c < length; c++) {
                System.out.print(arr[r][c] + "\t");
            }
            System.out.println();
        }

        int[][] ms = mst.minimumSpanningTree(arr);
        System.out.println("/nminimum spanning tree:");
        for (int r = 0; r < ms.length; r++) {
            for (int c = 0; c < ms.length; c++) {
                System.out.print(ms[r][c] + "\t");
            }
            System.out.println();
        }
        lines = new HashMap<>();
        values = new HashMap<>();
        valuesLocation = new HashMap<>();
        int id = 0;
        for (int r = 0; r < ms.length; r++) {
            for (int c = 0; c < ms.length; c++) {
                if (ms[r][c] != 0 && ms[r][c] != 99) {
                    Integer ns[] = {r, c};
                    lines.put(id, ns);
                    values.put(id, Integer.toString(ms[r][c]));
                    Point sp = nodesLocation.get(r);
                    Point ep = nodesLocation.get(c);
                    Point p = new Point((sp.x + ep.x) / 2, (sp.y + ep.y) / 2);
                    valuesLocation.put(id, p);
                    id++;
                }
            }
        }
        this.paint(gfx);
    }

    public void resetProgram() {
        nodeID = 0;
        lineID = 0;
        dragNodeID = -1;
        startNodeID = -1;
        endNodeID = -1;
        dragNode = false;
        startLineFlag = false;
        lineExist = false;
        status = -1;
        nodesLocation = new HashMap<>();
        lines = new HashMap<>();
        valuesLocation = new HashMap<>();
        values = new HashMap<>();
        this.paint(gfx);
    }

    public void drawToolBox() {
        gfx.setFont(new Font("Arial", Font.BOLD, 18));
        gfx.setColor(Color.BLUE);
        gfx.drawString("Current Operation", 30, 650);
        switch (status) {
            case CREATE_NODE:
                gfx.drawImage(node, 50, 670, rootPane);
                break;
            case DELETE_NODE:
                gfx.drawImage(deleteNode, 50, 670, rootPane);
                break;
            case CONNECT_NODES:
                gfx.drawImage(connect, 50, 670, rootPane);
                break;
            case DRAG_NODE:
                gfx.drawImage(cursor, 50, 670, rootPane);
                break;
            case LOAD:
                gfx.drawImage(load, 50, 670, rootPane);
                break;
            case SAVE:
                gfx.drawImage(save, 50, 670, rootPane);
                break;
            default:
                break;
        }
        gfx.drawString("Solve Network", 250, 650);
        gfx.drawImage(solve, 270, 670, rootPane);
        gfx.drawString("Load Project", 470, 650);
        gfx.drawImage(load, 490, 670, rootPane);
        gfx.drawString("Save Project", 690, 650);
        gfx.drawImage(save, 710, 670, rootPane);
        gfx.setStroke(new BasicStroke(2));
        gfx.setColor(Color.BLACK);
        gfx.drawLine(30, 620, 1000, 620);
        gfx.drawLine(1180, 40, 1180, 630);
        gfx.setColor(Color.RED);
        gfx.drawLine(30, 622, 1000, 622);
        gfx.drawLine(1182, 40, 1182, 630);
        gfx.setFont(new Font("Arial", Font.BOLD, 18));
        gfx.setColor(Color.BLUE);
        gfx.drawString("Create Node", 1200, 50);
        gfx.drawImage(node, 1220, 60, rootPane);
        gfx.drawString("Connect Nodes", 1200, 150);
        gfx.drawImage(connect, 1220, 160, rootPane);
        gfx.drawString("Delete Nodes", 1200, 250);
        gfx.drawImage(deleteNode, 1220, 260, rootPane);
        gfx.drawString("Drag & SetValue", 1200, 350);
        gfx.drawImage(cursor, 1220, 360, rootPane);
        gfx.drawString("Reset Program", 1200, 450);
        gfx.drawImage(reset, 1220, 460, rootPane);
        gfx.drawString("Exit Program", 1200, 550);
        gfx.drawImage(exit, 1220, 560, rootPane);
    }

    @Override
    public void paint(Graphics g) {
        gfx.drawImage(backGround, 0, 0, this);
        this.drawToolBox();
        this.drawAllLines();
        this.drawAllNodes();
        this.drawAllValues();
    }

    public void loadProject() {
        String fileName = "";
        JFileChooser fc = new JFileChooser();
        int ret = fc.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getPath();
            System.out.println(fileName);
        }
        if ("".equals(fileName)) {
            return;
        }
        try {
            File f = new File(fileName);
            Scanner sf = new Scanner(f);
            nodesLocation = new HashMap<>();
            while (!"lines".equals(sf.nextLine())) {
                Integer id = sf.nextInt();
                Point p = new Point(sf.nextInt(), sf.nextInt());
                nodesLocation.put(id, p);
            }
            this.paint(gfx);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }

    }

    public void saveProject() {
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            fw = new FileWriter("networkProject.txt");
            pw = new PrintWriter(fw);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (pw == null) {
            return;
        }
        pw.println("nodesLocation");
        for (int i = 0; i < nodesLocation.size(); i++) {
            Point p = null;
            pw.println(nodesLocation.keySet().toArray()[i]);
            p = (Point) nodesLocation.values().toArray()[i];
            pw.println(p.x);
            pw.println(p.y);
            if (i != nodesLocation.size() - 1) {
                pw.println();
            }
        }
        pw.println("lines");
        for (int i = 0; i < lines.size(); i++) {
            pw.println(lines.keySet().toArray()[i]);
            pw.println(lines.values().toArray()[i]);
        }
        pw.println("values");
        for (int i = 0; i < values.size(); i++) {
            pw.println(values.keySet().toArray()[i]);
            pw.println(values.values().toArray()[i]);
        }
        pw.println("valuesLocation");
        for (int i = 0; i < valuesLocation.size(); i++) {
            pw.println(valuesLocation.keySet().toArray()[i]);
            pw.println(valuesLocation.values().toArray()[i]);
        }
        try {
            fw.close();
            pw.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.toString());
        }
    }
}