import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

public class ReportForm {
    public JPanel reportPanel;
    private JTable table1;
    private JButton btnBack;
    private JButton btnExport;
    private DefaultTableModel model;
    private CRUD crud;

    public ReportForm(JFrame frame) {
        crud = new CRUD(); // Instanciamos el CRUD

        // Configurar modelo de la tabla
        String[] columnHeaders = {"Usuario", "Dirección", "Producto ID", "Nombre Producto", "Total", "Estado"};
        model = new DefaultTableModel(columnHeaders, 0);
        table1.setModel(model);

        // Cargar los datos al abrir el formulario
        cargarPedidos();

        // Acción de retroceder
        btnBack.addActionListener(e -> {
            frame.setContentPane(new AdminForm(frame).adminPanel);
            frame.revalidate();
            frame.repaint();
        });

        // Acción de exportar PDF
        btnExport.addActionListener(e -> exportarPDF());
    }

    private void cargarPedidos() {
        crud.obtenerPedidosConDetalles(model); // Cargamos los pedidos en la tabla
    }

    private void exportarPDF() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un pedido para exportar.");
            return;
        }

        int orderId = (int) table1.getValueAt(selectedRow, 2); // Obtener el product_id (ID de la orden)

        // Usamos CRUD para obtener los detalles del pedido
        Object[] pedido = crud.obtenerDetallePedido(orderId);
        if (pedido == null) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar el pedido.");
            return;
        }

        // Usar JFileChooser para elegir la ubicación donde guardar el archivo
        JFileChooser saveDialog = new JFileChooser();
        saveDialog.setDialogTitle("Guardar Factura");
        saveDialog.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

        // Abrir cuadro de diálogo de guardado
        int userSelection = saveDialog.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // Si el usuario no selecciona "Guardar", no hacemos nada
        }

        // Obtener la ruta del archivo seleccionado
        File fileToSave = saveDialog.getSelectedFile();
        if (!fileToSave.getName().endsWith(".pdf")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
        }

        // Crear un documento PDF
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new java.io.FileOutputStream(fileToSave));
            document.open();

            // Título de la factura
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Factura - Pedido " + orderId, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Fecha de la factura
            Font dateFont = new Font(Font.FontFamily.HELVETICA, 12);
            Paragraph date = new Paragraph("Fecha: " + java.time.LocalDate.now(), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);

            document.add(new Paragraph("\n")); // Espacio entre sección de fecha y cliente

            // Información del cliente
            Font clientFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph("Cliente: " + pedido[0], clientFont));
            document.add(new Paragraph("Dirección: " + pedido[1], clientFont));
            document.add(new Paragraph("\n"));

            // Detalle de la tabla de productos
            PdfPTable table = new PdfPTable(4); // 4 columnas: Producto, Cantidad, Precio Unitario, Total

            // Añadir encabezados de la tabla
            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio Unitario");
            table.addCell("Total");

            // Detalles del producto (suponiendo que `pedido` tiene la información necesaria)
            String producto = (String) pedido[3];
            int cantidad = (int) pedido[5]; // Usamos la cantidad del pedido
            double precioUnitario = (double) pedido[4]; // Precio unitario obtenido desde la base de datos
            double total = precioUnitario * cantidad; // El total es cantidad * precio unitario

            // Formato decimal para el precio y total
            DecimalFormat df = new DecimalFormat("0.00");
            table.addCell(producto);
            table.addCell(String.valueOf(cantidad)); // Muestra la cantidad correcta
            table.addCell("$" + df.format(precioUnitario)); // Muestra el precio unitario correcto
            table.addCell("$" + df.format(total)); // Muestra el total correcto

            document.add(table); // Añadir la tabla de productos

            document.add(new Paragraph("\n")); // Espacio

            // Estado y total de la factura


            double totalFactura = total;
            document.add(new Paragraph("SubTotal: $" + df.format(totalFactura), clientFont));
            document.add(new Paragraph("Total: " + pedido[6], clientFont));
            // Cerrar documento
            document.close();

            JOptionPane.showMessageDialog(null, "Factura exportada como PDF.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF.");
        }
    }
}
