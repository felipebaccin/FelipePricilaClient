package br.univel.swing;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import br.univel.model.Curso;
import br.univel.model.Disciplina;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CursoTela extends JFrame {

	private JPanel contentPane;
	private JTextField txtCurso;
	private JTable table;
	private ModeloCurso mc;
	private static final String ENDERECO_SERVICE = "http://jbosswildfly-felipebaccin.rhcloud.com/faculdade/rest/cursos";
	private Long id;
	private JButton btnSalvar;
	private JButton btnApagar;
	private List<Curso> cursos;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CursoTela frame = new CursoTela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CursoTela() {
		
		id = new Long(0);
		cursos = new ArrayList<Curso>();
		mc = new ModeloCurso(cursos);
		
		try {
			resgataCurso();
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblCurso = new JLabel("Curso");
		GridBagConstraints gbc_lblCurso = new GridBagConstraints();
		gbc_lblCurso.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurso.anchor = GridBagConstraints.EAST;
		gbc_lblCurso.gridx = 0;
		gbc_lblCurso.gridy = 0;
		panel.add(lblCurso, gbc_lblCurso);
		
		txtCurso = new JTextField();
		GridBagConstraints gbc_txtCurso = new GridBagConstraints();
		gbc_txtCurso.insets = new Insets(0, 0, 5, 5);
		gbc_txtCurso.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCurso.gridx = 1;
		gbc_txtCurso.gridy = 0;
		panel.add(txtCurso, gbc_txtCurso);
		txtCurso.setColumns(10);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtCurso.getText().isEmpty()) {
					Curso c = new Curso();
					c.setNome(txtCurso.getText());
				if (!(id.intValue()==0))
					c.setId(id);
					id = new Long(0);
					try {
						salvarCursos(c);
						txtCurso.setText("");
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(), "Deu Pau", JOptionPane.ERROR_MESSAGE);
					}
					try {
						resgataCurso();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Ta vazio", "Vish", JOptionPane.ERROR_MESSAGE);
				}
				btnApagar.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnSalvar = new GridBagConstraints();
		gbc_btnSalvar.insets = new Insets(0, 0, 5, 0);
		gbc_btnSalvar.gridx = 2;
		gbc_btnSalvar.gridy = 0;
		panel.add(btnSalvar, gbc_btnSalvar);
		
		btnApagar = new JButton("Apagar");
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (id.intValue()>0) {
					if(JOptionPane.showConfirmDialog(null, "Se que excluir essa bagaça","vai memo",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
						return;
					}
					try {
						apagarCurso(id.intValue());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Deu pau");
					}
				}
				id = new Long(0);
				txtCurso.setText("");
				btnApagar.setEnabled(false);
				try {
					resgataCurso();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		GridBagConstraints gbc_btnApagar = new GridBagConstraints();
		gbc_btnApagar.insets = new Insets(0, 0, 5, 0);
		gbc_btnApagar.gridx = 2;
		gbc_btnApagar.gridy = 1;
		panel.add(btnApagar, gbc_btnApagar);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		panel.add(scrollPane, gbc_scrollPane);
		
		table = new JTable(mc);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					if (table.getRowCount()>0) {
						Curso c = cursos.get(table.getSelectedRow());
						txtCurso.setText(c.getNome());
						id = c.getId();
						btnApagar.setEnabled(true);
					}
				}
				
			}
		});
		table.setSize(new Dimension(0, 150));
		scrollPane.setViewportView(table);
	}

	private void apagarCurso(int id) throws Exception {
		ClientRequest request = new ClientRequest(ENDERECO_SERVICE+"/"+id);
		request.accept("aplication/json");
		ClientResponse<String> response = request.delete(String.class);
		if ((response.getStatus()!=200) && (response.getStatus()!=204)) {
			throw new RuntimeException("Failed: HTTP error code: "+response.getStatus());
		}
	}

	private void resgataCurso() throws Exception{
		cursos.clear();
		ClientRequest request = new ClientRequest(ENDERECO_SERVICE);
		request.accept("application/json");
		ClientResponse<String> response = request.get(String.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			new ByteArrayInputStream(response.getEntity().getBytes())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			ObjectMapper mapper = new ObjectMapper();
			Curso[] curso = mapper.readValue(output, Curso[].class);
			for (Curso c : curso){
				cursos.add(c);
			}
			System.out.println(output+"\n");
		}
		mc.fireTableDataChanged();
	}

	private void salvarCursos(Curso c)throws Exception {
		ClientRequest request = new ClientRequest(ENDERECO_SERVICE);
		if (c.getId() == null){
			request = new ClientRequest(ENDERECO_SERVICE);
		} else {
			request = new ClientRequest(ENDERECO_SERVICE + "/" + c.getId().toString());
		} 
		request.accept("application/json");
		ObjectMapper om = new ObjectMapper();
		String retorno = om.writeValueAsString(c);
		request.body("application/json", retorno);
		ClientResponse<String> response;
		if (c.getId() == null){
			response = request.post(String.class);
		} else {
			response = request.put(String.class);
		}
		if ((response.getStatus() != 201) && (response.getStatus() != 204)) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
		}
	}

}
