import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GUI {

    private JPanel panel1;
    private JTextField txtNo;
    private JTextField txtName;
    private JTextField txtMonthlyIncome;
    private JRadioButton rdbMarried;
    private JRadioButton rdbSingle;
    private JCheckBox chkMember;
    private JTable table1;
    private JTextField txtIncome;
    private JTextField txtTax;
    private JTextField txtAfterTax;
    private JScrollPane jtable1;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnEdit;

    //add a frame

    JFrame frame = new JFrame();

    //create an object out from the connection
    Connection1 con = new Connection1();

    //create the connection

    Connection c1 = con.connect();

    public GUI() throws SQLException, ClassNotFoundException {

        loadtable();


        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String eno,ename,emonthly,estatus="",emember="";


                eno = txtNo.getText();
                ename = txtName.getText();
                emonthly = txtMonthlyIncome.getText();

                if(rdbMarried.isSelected()){
                    estatus = "Married";
                }

                if(rdbSingle.isSelected()){
                    estatus = "Single";
                }

                if(chkMember.isSelected()){
                    emember = "Yes";
                }
                else{
                    emember = "No";

                }

                //validate entry

                if(txtNo.getText().equals("") || txtName.getText().equals("") || txtMonthlyIncome.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"You must enter data ");
                    return;
                }

                //check if data is already existing

                String quercheck = "Select * from tbl_emp where emp_no=?";

                try {
                    PreparedStatement qq = c1.prepareStatement(quercheck);
                    qq.setString(1, eno);


                    ResultSet rs = qq.executeQuery();
                    rs.last();

                    int cnt = rs.getRow();

                    if(cnt>0){
                        JOptionPane.showMessageDialog(null,"The record is already existing..");
                        txtNo.setText("");
                        txtName.setText("");
                        txtMonthlyIncome.setText("");

                        return;

                    }


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


                String quer1 = "Insert into tbl_emp values (?,?,?,?,?)";
                try {
                    PreparedStatement query = c1.prepareStatement(quer1);
                    query.setString(1, eno);
                    query.setString(2,ename);
                    query.setString(3,emonthly);
                    query.setString(4,estatus);
                    query.setString(5,emember);

                    query.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Once record added ");
                    loadtable();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                loadtable();

                txtNo.setText("");
                txtName.setText("");
                txtMonthlyIncome.setText("");




            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eno,ename,emonthly,estatus="",emember="";

                DefaultTableModel df = (DefaultTableModel) table1.getModel();
                int mIndex1 = table1.getSelectedRow();



                if(txtName.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"There is no record to edit ");
                    return;
                }

                eno = txtNo.getText();
                ename = txtName .getText();
                emonthly = txtMonthlyIncome.getText();

                String oldvalue= df.getValueAt(mIndex1,0).toString();

                if(rdbMarried.isSelected()){
                    estatus = "Married";
                }

                if(rdbSingle.isSelected()){
                    estatus = "Single";
                }

                if(chkMember.isSelected()){
                    emember = "Yes";
                }
                else{
                    emember = "No";

                }



                String quer1 = "Update tbl_emp set emp_no=?, emp_name=?," +
                        " emp_income=?,emp_status=?, emp_type=? where emp_no =?";



                try {
                    PreparedStatement query = c1.prepareStatement(quer1);
                    query.setString(1, eno);
                    query.setString(2,ename);
                    query.setString(3,emonthly);
                    query.setString(4,estatus);
                    query.setString(5,emember);
                    query.setString(6,oldvalue);

                    query.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Once record edited ");
                    loadtable();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                loadtable();

                txtNo.setText("");
                txtName.setText("");
                txtMonthlyIncome.setText("");



            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtNo.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"There is no record to delete ");
                    return;
                }



                String  eno;


                eno = txtNo.getText();


                String quer1 = "Delete from tbl_emp where emp_no =?";
                try {
                    PreparedStatement query = c1.prepareStatement(quer1);

                    query.setString(1,eno);

                    int input = JOptionPane.showConfirmDialog(null,
                            "Do you want to delete the record?", "Select an Option...",JOptionPane.YES_NO_CANCEL_OPTION);

                    if(input==1 || input==2){
                        loadtable();
                        return;
                    }
                    query.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Once record deleted ");
                    loadtable();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                loadtable();

                txtNo.setText("");
                txtName.setText("");
                txtIncome.setText("");

                rdbSingle.setSelected(false);
                rdbMarried.setSelected(false);
                chkMember.setSelected(false);


            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DefaultTableModel df = (DefaultTableModel) table1.getModel();
                int index1 = table1.getSelectedRow();
                String mStat,mMember;
                txtNo.setText(df.getValueAt(index1,0).toString());
                txtName.setText(df.getValueAt(index1,1).toString());
                txtMonthlyIncome.setText(df.getValueAt(index1,2).toString());


                mStat = String.format(df.getValueAt(index1,3).toString());
                mMember = String.format(df.getValueAt(index1,4).toString());

                if(mStat.equals("Single")){
                    rdbSingle.setSelected(true);
                }
                if(mStat.equals("Married")){
                    rdbMarried.setSelected(true);
                }

                if(mMember.equals("Yes")){
                    chkMember.setSelected(true);
                }

                if(mMember.equals("No")){
                    chkMember.setSelected(false);
                }


            }
        });
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {


        GUI obj1 = new GUI();
        obj1.callform();

    }

    public void callform() throws SQLException, ClassNotFoundException {

        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void loadtable(){

        String columns[]={"Emp number", "Name", "Income","Status","Member Type"};
        String quer1 = "Select * from tbl_emp order by emp_no";
        String[][] array;

        ResultSet rs;
        try {
            PreparedStatement query = c1.prepareStatement(quer1);

            rs = query.executeQuery();

            int rowcount;

            rs.last();

            rowcount = rs.getRow();

            rs.beforeFirst();;


            array = new String[rowcount][5];

            int j=0;


            while(rs.next()){

                array[j][0] = rs.getString("emp_no");
                array[j][1] = rs.getString("emp_name");
                array[j][2] = rs.getString("emp_income");
                array[j][3] = rs.getString("emp_status");
                array[j][4] = rs.getString("emp_type");
                ++j;
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        DefaultTableModel model1 = new DefaultTableModel(array, columns);
        table1.setModel(model1);

        table1.setDefaultEditor(Object.class,null);

    }

}
