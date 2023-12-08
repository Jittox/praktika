package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AutostationGUI extends JFrame {

    private JTextArea textArea;
    private JComboBox<String> dataTypeComboBox;

    public AutostationGUI() {
        setTitle("Автовокзал");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);

        dataTypeComboBox = new JComboBox<>(new String[]{"Водители", "Автобусы", "Маршруты"});
        dataTypeComboBox.setSelectedIndex(0); // По умолчанию выбраны водители

        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Кнопка для запуска операции в фоновом режиме - Получить данные
        JButton fetchDataButton = new JButton("Получить данные");
        fetchDataButton.addActionListener(e -> fetchDataInBackground());
        buttonPanel.add(fetchDataButton);

        // Кнопка для запуска операции в фоновом режиме - Добавить данные
        JButton addDataButton = new JButton("Добавить данные");
        addDataButton.addActionListener(e -> addDataInBackground());
        buttonPanel.add(addDataButton);
        // Кнопка для запуска операции в фоновом режиме - Удалить данные
        JButton deleteDataButton = new JButton("Удалить данные");
        deleteDataButton.addActionListener(e -> showDeleteDataDialog());
        buttonPanel.add(deleteDataButton);
        add(dataTypeComboBox, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void showDeleteDataDialog() {
        JTextField idTextField = new JTextField();
        Object[] message = {"Введите ID:", idTextField};

        int option = JOptionPane.showConfirmDialog(null, message, "Удаление данных", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            deleteDataInBackground(idTextField.getText());
        }
    }
    private void fetchDataInBackground() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                fetchData();
                return null;
            }

            @Override
            protected void done() {
                // Вызывается после завершения фоновой операции

            }
        };

        worker.execute();
    }
    private void addDataInBackground() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                addData();
                return null;
            }

            @Override
            protected void done() {
                // Вызывается после завершения фоновой операции

            }
        };

        worker.execute();
    }
    private void deleteDataInBackground(String idToDelete) {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                deleteData(idToDelete);
                return null;
            }

            @Override
            protected void done() {
                // Вызывается после завершения фоновой операции
                }
        };

        worker.execute();
    }
    private void fetchData() {
        String url = "jdbc:postgresql://localhost:5432/Avtovokzal1";
        String user = "user1";
        String password = "1";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Соединение с базой данных установлено.");

                // Вывод данных в зависимости от выбранного типа данных
                String selectedDataType = (String) dataTypeComboBox.getSelectedItem();
                switch (selectedDataType) {
                    case "Водители":
                        clearTextArea();
                        printDriverData(connection);
                        break;
                    case "Автобусы":
                        clearTextArea();
                        printBusData(connection);
                        break;
                    case "Маршруты":
                        clearTextArea();
                        printRouteData(connection);
                        break;
                    default:
                        break;
                }

                System.out.println("Данные успешно получены.");
            } else {
                System.out.println("Не удалось установить соединение с базой данных.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    private void printDriverData(Connection connection) throws SQLException {
        appendTextToTextArea("Таблица Водитель:");
        printTableData(connection, "Водитель");
    }

    private void printBusData(Connection connection) throws SQLException {
        appendTextToTextArea("Таблица Автобус:");
        printTableData(connection, "Автобус");
    }

    private void printRouteData(Connection connection) throws SQLException {
        appendTextToTextArea("Таблица Маршрут:");
        printTableData(connection, "Маршрут");
    }

    private void printTableData(Connection connection, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            clearTextArea();
            appendTextToTextArea("Таблица " + tableName + ":");

            while (resultSet.next()) {
                StringBuilder rowData = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    rowData.append(columnName).append(": ").append(columnValue).append(", ");
                }

                rowData.delete(rowData.length() - 2, rowData.length());

                appendTextToTextArea(rowData.toString());
            }
        }
    }


    private void appendTextToTextArea(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AutostationGUI autostationGUI = new AutostationGUI();
            autostationGUI.setVisible(true);
        });
    }
    private void clearTextArea() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("");
        });
    }
    private void addData() {
        String url = "jdbc:postgresql://localhost:5432/Avtovokzal1";
        String user = "user1";
        String password = "1";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Соединение с базой данных установлено.");

                // Вывод данных в зависимости от выбранного типа данных
                String selectedDataType = (String) dataTypeComboBox.getSelectedItem();

                switch (selectedDataType) {
                    case "Водители":
                        addDriverData(connection);
                        break;
                    case "Автобусы":
                        addBusData(connection);
                        break;
                    case "Маршруты":
                        addRouteData(connection);
                        break;
                    default:
                        break;
                }

                System.out.println("Данные успешно добавлены.");
            } else {
                System.out.println("Не удалось установить соединение с базой данных.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    private void addDriverData(Connection connection) {
        try {
            String lastName = JOptionPane.showInputDialog("Введите фамилию водителя:");
            String firstName = JOptionPane.showInputDialog("Введите имя водителя:");
            String middleName = JOptionPane.showInputDialog("Введите отчество водителя:");
            String phoneNumber = JOptionPane.showInputDialog("Введите номер телефона водителя:");
            String busNumber = JOptionPane.showInputDialog("Введите номер автобуса:");

            String insertQuery = "INSERT INTO Водитель (фамилия, имя, отчество, номер_телефона, номер_автобуса) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, lastName);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, middleName);
                preparedStatement.setString(4, phoneNumber);
                preparedStatement.setString(5, busNumber);

                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Данные водителя успешно добавлены.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при добавлении данных водителя: " + e.getMessage());
        }
    }

    private void addBusData(Connection connection) {
        try {
            int driver = Integer.parseInt(JOptionPane.showInputDialog("Введите id водителя автобуса:"));
            String routeNumber = JOptionPane.showInputDialog("Введите номер маршрута:");
            String vehicleNumber = JOptionPane.showInputDialog("Введите номер транспортного средства (автобуса):");

            String insertQuery = "INSERT INTO Автобус (водитель, номер_маршрута, номер_транспортного_средства) " +
                    "VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, driver);
                preparedStatement.setString(2, routeNumber);
                preparedStatement.setString(3, vehicleNumber);

                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Данные автобуса успешно добавлены.");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при добавлении данных автобуса: " + e.getMessage());
        }
    }



    private void addRouteData(Connection connection) {
        try {
            String routeName = JOptionPane.showInputDialog("Введите название маршрута:");
            String routeType = JOptionPane.showInputDialog("Введите тип маршрута (городской/междугородний):");
            int stopsCount = Integer.parseInt(JOptionPane.showInputDialog("Введите количество остановок:"));
            int busId = Integer.parseInt(JOptionPane.showInputDialog("Введите ID автобуса:"));

            String insertQuery = "INSERT INTO Маршрут (название_маршрута, тип_маршрута, количество_остановок, автобус) " +
                    "VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, routeName);
                preparedStatement.setString(2, routeType);
                preparedStatement.setInt(3, stopsCount);
                preparedStatement.setInt(4, busId);

                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Данные маршрута успешно добавлены.");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при добавлении данных маршрута: " + e.getMessage());
        }
    }


    private void deleteData(String idToDelete) {
        String url = "jdbc:postgresql://localhost:5432/Avtovokzal1";
        String user = "user1";
        String password = "1";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Соединение с базой данных установлено.");

                // Удаление данных в зависимости от выбранного типа данных
                String selectedDataType = (String) dataTypeComboBox.getSelectedItem();
                switch (selectedDataType) {
                    case "Водители":
                        deleteDriverData(connection, idToDelete);
                        break;
                    case "Автобусы":
                        deleteBusData(connection, idToDelete);
                        break;
                    case "Маршруты":
                        deleteRouteData(connection, idToDelete);
                        break;
                    default:
                        // Неизвестный тип данных
                        break;
                }

                System.out.println("Данные успешно удалены.");
            } else {
                System.out.println("Не удалось установить соединение с базой данных.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    // Метод удаления данных по ID из таблицы Водитель
    private void deleteDriverData(Connection connection, String idToDelete) {
        try {
            int id = Integer.parseInt(idToDelete);

            String deleteQuery = "DELETE FROM Водитель WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Данные водителя с ID " + id + " успешно удалены.");
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при удалении данных водителя: " + e.getMessage());
        }
    }

    // Метод удаления данных по ID из таблицы Автобус
    private void deleteBusData(Connection connection, String idToDelete) {
        try {
            int id = Integer.parseInt(idToDelete);

            String deleteQuery = "DELETE FROM Автобус WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Данные автобуса с ID " + id + " успешно удалены.");
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при удалении данных автобуса: " + e.getMessage());
        }
    }
    // Метод удаления данных по ID из таблицы Маршрут
    private void deleteRouteData(Connection connection, String idToDelete) {
        try {
            int id = Integer.parseInt(idToDelete);

            String deleteQuery = "DELETE FROM Маршрут WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Данные маршрута с ID " + id + " успешно удалены.");
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при удалении данных маршрута: " + e.getMessage());
        }
    }

}
