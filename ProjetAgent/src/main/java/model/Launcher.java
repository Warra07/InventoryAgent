package model;

import agent.*;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.ParallelBehaviour;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class    Launcher {

    public static void main(String[] args) {


        Runtime runtime = Runtime.instance();
        Profile config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "true");
        AgentContainer mc = runtime.createMainContainer(config);

        AgentController acInvServ;
        AgentController acClient;


        try {

            acInvServ = mc.createNewAgent("InventoryAgent", InventoryAgent.class.getName(), null);
            acClient = mc.createNewAgent("ClientAgent", ClientAgent.class.getName(), null);



            acInvServ.start();
            acClient.start();

        }
        catch (StaleProxyException e) { }   } }