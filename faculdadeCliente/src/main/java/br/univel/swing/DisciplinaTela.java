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

import br.univel.model.Disciplina;
import br.univel.model.Disciplina;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DisciplinaTela extends JFrame {

	private JPanel contentPane;
	private JTextField txtDisciplina;
	private JTable table;
	private ModeloDisciplina md;
	private static final String ENDERECO_SERVICE = "http://jbosswildfly-felipebaccin.rhcloud.com/faculdade/rest/disciplinas";
	private Long id;
	private JButton btnSalvar;
	private JButton btnApagar;
	private List<Disciplina> disciplinas;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisciplinaTela frame = new DisciplinaTela();
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
	public DisciplinaTela() {
		
		md = new ModeloDisciplina(disciplinas);
		
		id = new Long(0);
		disciplinas = new ArrayList<Disciplina>();
		md = new ModeloDisciplina(disciplinas);
		
		try {
			resgataDisciplina();
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
		
		JLabel lblDisciplina = new JLabel("Disciplina");
		GridBagConstraints gbc_lblDisciplina = new GridBagConstraints();
		gbc_lblDisciplina.insets = new Insets(0, 0, 5, 5);
		gbc_lblDisciplina.anchor = GridBagConstraints.EAST;
		gbc_lblDisciplina.gridx = 0;
		gbc_lblDisciplina.gridy = 0;
		panel.add(lblDisciplina, gbc_lblDisciplina);
		
		txtDisciplina = new JTextField();
		GridBagConstraints gbc_txtDisciplina = new GridBagConstraints();
		gbc_txtDisciplina.insets = new Insets(0, 0, 5, 5);
		gbc_txtDisciplina.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDisciplina.gridx = 1;
		gbc_txtDisciplina.gridy = 0;
		panel.add(txtDisciplina, gbc_txtDisciplina);
		txtDisciplina.setColumns(10);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtDisciplina.getText().isEmpty()) {
					Disciplina d = new Disciplina();
					d.setNome(txtDisciplina.getText());
				if (!(id.intValue()==0))
					d.setId(id);
					id = new Long(0);
					try {
						salvarDisciplina(d);
						txtDisciplina.setText("");
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(), "Deu Pau", JOptionPane.ERROR_MESSAGE);
					}
					try {
						resgataDisciplina();
					} catch (Exception e1) {
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
						apagarDisciplina(id.intValue());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Deu pau");
					}
				}
				id = new Long(0);
				txtDisciplina.setText("");
				btnApagar.setEnabled(false);
				try {
					resgataDisciplina();
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
		
		table = new JTable(md);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					if (table.getRowCount()>0) {
						Disciplina d = disciplinas.get(table.getSelectedRow());
						txtDisciplina.setText(d.getNome());
						id = d.getId();
						btnApagar.setEnabled(true);
					}
				}
				
			}
		});
		table.setSize(new Dimension(0, 150));
		scrollPane.setViewportView(table);
	}

	private void apagarDisciplina(int id) throws Exception {
		ClientRequest request = new ClientRequest(ENDERECO_SERVICE+"/"+id);
		request.accept("aplication/json");
		ClientResponse<String> response = request.delete(String.class);
		if ((response.getStatus()!=200) && (response.getStatus()!=204)) {
			throw new RuntimeException("Failed: HTTP error code: "+response.getStatus());
		}
	}

	private void resgataDisciplina() throws Exception{
		disciplinas.clear();
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
			Disciplina[] disciplina = mapper.readValue(output, Disciplina[].class);
			for (Disciplina d : disciplina){
				disciplinas.add(d);
			}
			System.out.println(output+"\n");
		}
		md.fireTableDataChanged();
	}

	private void salvarDisciplina(Disciplina d)throws Exception {
		ClientRequest request = new ClientRequest(ENDERECO_SERVICE);
		if (d.getId() == null){
			request = new ClientRequest(ENDERECO_SERVICE);
		} else {
			request = new ClientRequest(ENDERECO_SERVICE + "/" + d.getId().toString());
		} 
		request.accept("application/json");
		ObjectMapper om = new ObjectMapper();
		String retorno = om.writeValueAsString(d);
		request.body("application/json", retorno);
		ClientResponse<String> response;
		if (d.getId() == null){
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
