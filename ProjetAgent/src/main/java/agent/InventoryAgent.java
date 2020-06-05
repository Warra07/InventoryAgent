package agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;
import org.glassfish.jersey.client.ClientResponse;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryAgent extends Agent implements AgentVocabulary {
    ArrayList products;
    ArrayList orders;
    ArrayList clients;
    jakarta.ws.rs.client.Client apiClient = ClientBuilder.newClient();
    final String apiUrl = "http://localhost:9090";
    protected void setup() {

        products = apiClient.target(apiUrl + "/products").request(MediaType.APPLICATION_JSON_TYPE)
                .get(Response.class)
                .readEntity(new GenericType<ArrayList<Product>>() {});
        orders = apiClient.target(apiUrl + "/orders").request(MediaType.APPLICATION_JSON_TYPE)
                .get(Response.class)
                .readEntity(new GenericType<ArrayList<Order>>() {});
        clients = apiClient.target(apiUrl + "/clients").request(MediaType.APPLICATION_JSON_TYPE)
                .get(Response.class)
                .readEntity(new GenericType<ArrayList<Client>>() {});




        // Set this agent main behaviour
        SequentialBehaviour sb = new SequentialBehaviour();
        sb.addSubBehaviour(new RegisterInDF(this));
        sb.addSubBehaviour(new ReceiveMessages(this));
        addBehaviour(sb);

    }

    class RegisterInDF extends OneShotBehaviour {
        RegisterInDF(Agent a) {
            super(a);
        }

        public void action() {

            ServiceDescription sd = new ServiceDescription();
            sd.setType(SERVER_AGENT);
            sd.setName(getName());
            sd.setOwnership("Prof6802");
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            dfd.addServices(sd);
            try {
                DFAgentDescription[] dfds = DFService.search(myAgent, dfd);
                if (dfds.length > 0 ) {
                    DFService.deregister(myAgent, dfd);
                }
                DFService.register(myAgent, dfd);
                System.out.println(getLocalName() + " is ready.");
            }
            catch (Exception ex) {
                System.out.println("Failed registering with DF! Shutting down...");
                System.out.println(ex.getMessage());
                doDelete();
            }
        }
    }


    class ReceiveMessages extends CyclicBehaviour {


        public ReceiveMessages(Agent a) {

            super(a);
        }

        public void action() {

            ACLMessage msg = receive();
            if (msg == null) { block(); return; }
            try {
                Object content = msg.getContentObject();
                switch (msg.getPerformative()) {

                    case (ACLMessage.REQUEST):

                        System.out.println("Request from " + msg.getSender().getLocalName());

                        if (content instanceof Client)
                            addBehaviour(new HandleCreateClient(myAgent, msg));
                        else if (content instanceof Order)
                            addBehaviour(new HandleCreateOrder(myAgent, msg));
                        else if (content instanceof Product)
                            addBehaviour(new HandleCreateProduct(myAgent, msg));
                        else
                            replyNotUnderstood(msg);
                        break;

                    case (ACLMessage.QUERY_REF):

                        System.out.println("Query from " + msg.getSender().getLocalName());

                        if (content instanceof Information)
                            addBehaviour(new HandleInformation(myAgent, msg));
                        else
                            replyNotUnderstood(msg);
                        break;

                    default: replyNotUnderstood(msg);
                }
            }
            catch(Exception ex) { System.out.println(ex.getMessage());}
        }
    }



    class HandleCreateClient extends OneShotBehaviour {
        private ACLMessage request;

        HandleCreateClient(Agent a, ACLMessage request) {

            super(a);
            this.request = request;
        }

        public void action() {

            try {
                Client client = (Client) request.getContentObject();
                apiClient.target(apiUrl + "/clients").request(MediaType.APPLICATION_JSON).post(Entity.entity(client, MediaType.APPLICATION_JSON));
                clients = apiClient.target(apiUrl + "/clients").request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList<Client>>() {});

                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContentObject(client);
                send(reply);

                System.out.println("Client created!");
            }
            catch(Exception ex) { System.out.println(ex.getMessage()); }
        }
    }



    class HandleCreateOrder extends OneShotBehaviour {
        private ACLMessage request;

        HandleCreateOrder(Agent a, ACLMessage request) {

            super(a);
            this.request = request;
        }

        public void action() {

            try {
                Order order = (Order) request.getContentObject();
                ArrayList<Produit> prods = (ArrayList) order.getProduits();
                order.setProduits(new ArrayList<Produit>());

                for(int i = 0; i < prods.size(); i++) {
                    Product pr = apiClient.target(apiUrl + "/products/" + prods
                            .get(i).getId()).request(MediaType.APPLICATION_JSON)
                            .get(new GenericType<Product>() {});

                    Produit prod = new Produit();

                    prod.setQuantity(pr.getQuantity());
                    prod.setPrice(pr.getPrice());
                    prod.setnom(pr.getnom());
                    prod.setCategorie(pr.getCategorie());
                    order.getProduits().add(prod);

                }
                Client cl = apiClient.target(apiUrl + "/clients/" + order.getClient().getId()).request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<Client>() {});
                order.getClient().setPrenom(cl.getPrenom());
                order.getClient().setNom(cl.getNom());
                order.getClient().setEmail(cl.getEmail());

                apiClient.target(apiUrl + "/orders").request(MediaType.APPLICATION_JSON).post(Entity.entity(order, MediaType.APPLICATION_JSON));
                orders = apiClient.target(apiUrl + "/orders").request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList<Order>>() {});


                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContentObject(order);
                send(reply);

                System.out.println("Order created!");
            }
            catch(Exception ex) { System.out.println(ex.getMessage()); }
        }
    }


    class HandleCreateProduct extends OneShotBehaviour {
        private ACLMessage request;

        HandleCreateProduct(Agent a, ACLMessage request) {

            super(a);
            this.request = request;
        }

        public void action() {

            try {
                Product product = (Product) request.getContentObject();
                apiClient.target(apiUrl + "/products").request(MediaType.APPLICATION_JSON).post(Entity.entity(product, MediaType.APPLICATION_JSON));
                products = apiClient.target(apiUrl + "/products").request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList<Product>>() {});

                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContentObject(product);
                send(reply);

                System.out.println("Product created!");
            }
            catch(Exception ex) { System.out.println(ex.getMessage()); }
        }
    }


    class HandleInformation extends OneShotBehaviour {
        private ACLMessage query;

        HandleInformation(Agent a, ACLMessage query) {

            super(a);
            this.query = query;
        }

        public void action() {

            try {
                Information info = (Information) query.getContentObject();
                Object obj = processInformation(info);
                ACLMessage reply = null;
                if (obj == null) replyNotUnderstood(query);
                else {
                    reply = query.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContentObject((java.io.Serializable)obj);
                }
                send(reply);
                System.out.println("Information processed.");
            }
            catch(Exception ex) { System.out.println(ex.getMessage()); }
        }
    }

    void replyNotUnderstood(ACLMessage msg) {
// -----------------------------------------

        try {
            java.io.Serializable content = msg.getContentObject();
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            reply.setContentObject(content);
            send(reply);
        }
        catch(Exception ex) { System.out.println(ex.getMessage()); }
    }


    Object processInformation(Information info) {

        if (info.getType() == CLIENT) {
            Client cl = apiClient.target("http://localhost:9090/clients/" + info.getClientId()).request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class)
                    .readEntity(new GenericType<Client>() {});;
            System.out.println("requested client" + cl.getNom() + " " + cl.getPrenom());
            if (cl == null) return newProblem(CLIENT_NOT_FOUND);
            return cl;
        }


        else if (info.getType() == CLIENTS) {
            ClientList clList = new ClientList();
            clList.setClients((List) clients);
            return clList;
        }

        else if (info.getType() == PRODUIT) {
            ProduitList prList = new ProduitList();
            prList.setProduits((List) products);
            return prList;
        }

         else if (info.getType() == ORDER) {
            OrderList orList = new OrderList();
            orList.setOrders((List) orders);
            return orList;
        }
        return null;
    }


    Problem newProblem(int num) {
// -----------------------------

        String msg = "";

        if (num == CLIENT_NOT_FOUND)
            msg = PB_CLIENT_NOT_FOUND;

        else if (num == PRODUIT_NOT_FOUND)
            msg = PB_PRDUIT_NOT_FOUND;

        else if (num == ORDER_NOT_FOUND)
            msg = PB_ORDER_NOT_FOUND;

        else if (num == ILLEGAL_OPERATION)
            msg = PB_ILLEGAL_OPERATION;

        Problem prob = new Problem();
        prob.setNum(num);
        prob.setMsg(msg);
        return prob;
    }

}
