package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ClientAgent extends Agent implements AgentVocabulary {
// -------------------------------------------------------------------

    static final int WAIT = -1;
    static final int QUIT = 0;
    private int command = WAIT;
    private int cnt = 0;
    private AID server;
    private List<Client> clients = new ArrayList();
    private List<Order> orders = new ArrayList();
    private List<Product> products = new ArrayList();

    protected void setup() {

        addBehaviour(new WaitUserCommand(this));
    }

    class WaitUserCommand extends OneShotBehaviour {

        WaitUserCommand(Agent a) {
            super(a);
            command = WAIT;
        }

        public void action() {

            command = getUserChoice();
            try {
            if (command == QUIT) {
                System.out.println(getLocalName() + " is shutting down...Bye!");
                doDelete();
                System.exit(0);
            }
            if (command == NEW_ORDER)
                createOrder();
            else if (command == NEW_CLIENT)
                createClient();
            else if (command == NEW_PRODUCT)
                createProduct();

            else if (command == CLIENT || command == CLIENTS || command == ORDER || command == PRODUIT)
                queryInformation();

            else {
                System.out.println("Invalid choice!");
                addBehaviour(new WaitUserCommand(myAgent));
            }
        }
        catch( Exception ei)
        {
            System.out.println(ei.getMessage());
            addBehaviour(new WaitUserCommand(myAgent));
        }
        }
    }


    void createClient() {


        Client client = new Client();

        client.setEmail(getUserInput("\nClient Email: "));
        client.setPrenom(getUserInput("\nClient first name : "));
        client.setNom(getUserInput("\nClient last name: "));

        sendMessage(ACLMessage.REQUEST, client);
    }
    void createProduct() {


        Product product = new Product();

        product.setnom(getUserInput("\nProduct Name: "));
        product.setCategorie(getUserInput("\nProduct Category : "));
        product.setQuantity(Integer.parseInt(getUserInput("\nProduct Quantity: ")));
        product.setPrice(Integer.parseInt(getUserInput("\nProduct Price : ")));


        sendMessage(ACLMessage.REQUEST, product);
    }


    void createOrder() {


        Order order = new Order();
        order.setDate(new Date());

        Client client = new Client();
        client.setId(Long.parseLong(getUserInput("Client id : \n>")));


        String prids = getUserInput("Products IDs : (ex: id1 id2 id3 )\n>");
        String[] splited = prids.split("\\s+");
        order.setProduits(new ArrayList<Produit>());
        for(int i=0; i < splited.length; i++) {
            Produit pr = new Produit();
            pr.setId(Long.parseLong(splited[i]));
            order.getProduits().add(pr);
        }

        order.setClient(client);
        sendMessage(ACLMessage.REQUEST, order);
    }





    void queryInformation() {



            Information info = new Information();
            info.setType(command);
            if(command == CLIENT) {
                info.setClientId(Long.parseLong(getUserInput("Client id : ")));
            }
            sendMessage(ACLMessage.QUERY_REF, info);
            return;
    }


    class WaitServerResponse extends ParallelBehaviour {

        WaitServerResponse(Agent a) {

            super(a, 1);

            addSubBehaviour(new ReceiveResponse(myAgent));

            addSubBehaviour(new WakerBehaviour(myAgent, 5000) {

                protected void handleElapsedTimeout() {
                    System.out.println("\n\tNo response from server. Please, try later!");
                    addBehaviour(new WaitUserCommand(myAgent));
                }
            });
        }
    }


    class ReceiveResponse extends SimpleBehaviour {

        private boolean finished = false;

        ReceiveResponse(Agent a) {
            super(a);
        }

        public void action() {

            ACLMessage msg = receive(MessageTemplate.MatchSender(server));
            if (msg == null) { block(); return; }

            if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD){
                System.out.println("\n\tResponse from server: NOT UNDERSTOOD!");
            }
            else {
                try {
                    Object content = msg.getContentObject();

                    if (content instanceof Problem) {
                        System.out.println("\n\n\tResponse from server: " +
                                ((Problem)content).getMsg().toUpperCase());
                    }


                    else if (command == NEW_CLIENT) {

                        Client client = (Client) content;
                        clients.add(client);
                        System.out.println("\n\n\tResponse from server:\nClient [" +
                                client.toString() + "] created!");
                    }
                    else if (command == NEW_ORDER) {

                        Order order = (Order) content;
                        orders.add(order);
                        System.out.println("\n\n\tResponse from server:\nOrder [" +
                                order.toString() + "] created!");
                    }
                    else if (command == NEW_PRODUCT) {

                        Product product = (Product) content;
                        products.add(product);
                        System.out.println("\n\n\tResponse from server:\nproduct [" +
                                product.getnom() + "] created!");
                    }


                    else if (command == CLIENT || command == CLIENTS || command == ORDER || command == PRODUIT) {

                        if (msg.getPerformative() == ACLMessage.INFORM){
                            if (content instanceof Client) {
                                Client client = (Client)content;
                                System.out.println("\n\n\tResponse from server:\nClient [" +
                                        client.toString() + "]");
                            }

                            else if (content instanceof ClientList) {
                                List list = ((ClientList) content).getClients();
                                clients = list;
                                String s = "\n\n\tLIST OF Clients: \n";
                                for (Iterator it = list.iterator(); it.hasNext();){
                                    s += ((Client)it.next()).toString();
                                s += "\n";
                            }
                                System.out.println(s);
                            }

                            else if (content instanceof OrderList) {
                                List list = ((OrderList) content).getOrders();
                                orders = list;
                                String s = "\n\n\tLIST OF Orders: \n";
                                for (Iterator it = list.iterator(); it.hasNext();) {
                                    s += ((Order) it.next()).toString();
                                    s += "\n";
                                }

                                System.out.println(s);
                            }

                            else if (content instanceof ProduitList) {
                                List list = ((ProduitList) content).getProduits();
                                products = list;
                                String s = "\n\n\tLIST OF Products: \n";
                                for (Iterator it = list.iterator(); it.hasNext();){
                                    s += ((Product)it.next()).toString();
                                s += "\n";
                            }
                                System.out.println(s);
                            }
                            else System.out.println("\nUnexpected msg content from server!");
                        }
                        else  System.out.println("\nUnexpected msg from server!");
                    }
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            finished = true;
        }

        public boolean done() { return finished; }

        public int onEnd() {
            addBehaviour(new WaitUserCommand(myAgent));
            return 0;
        }
    }


    void lookupServer() {

        ServiceDescription sd = new ServiceDescription();
        sd.setType(SERVER_AGENT);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);
        try {
            DFAgentDescription[] dfds = DFService.search(this, dfd);
            if (dfds.length > 0 ) {
                server = dfds[0].getName();
                System.out.println("Localized server");
            }
            else  System.out.println("Couldn't localize server!");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed searching int the DF!");
        }
    }







    int getUserChoice() {

        System.out.print("\n\t<<---- INVENTORY - MENU ---->>" +
                "\n\n\t0. Terminate program" +
                "\n\t1. Get Client Info \n\t2. Get Products List" +
                "\n\t3. Get Orders List \n\t4. Get Clients List" +
                "\n\t5. Create New Product \n\t6. Create New Order" +
                "\n\t7. Create new Client \n\n> ");
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            String in = buf.readLine();
            return Integer.parseInt(in);
        }
        catch (Exception ex) { ex.printStackTrace(); }
        return 0;
    }


    String getUserInput(String msg) {

        System.out.print(msg);
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            return buf.readLine();
        }
        catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    void sendMessage(int performative, Object content) {

        if (server == null) lookupServer();
        if (server == null) {
            System.out.println("Unable to localize the server! Operation aborted!");
            return;
        }
        ACLMessage msg = new ACLMessage(performative);
        try {
            msg.setContentObject((java.io.Serializable)content);
            msg.addReceiver(server);
            System.out.println("Contacting server... Please wait!");
            send(msg);
            addBehaviour(new WaitServerResponse(this));
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }

}