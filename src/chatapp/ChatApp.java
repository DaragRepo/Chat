/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

/**
 *
 * @author moh
 */
public class ChatApp {

    public static void main(String[] args) {
        ClientView view = new ClientView();
        ClientModel model = new ClientModel();

        ClientController controller = new ClientController(view, model);

        controller.reciveMessagesFromServer();

    }

}
