/*
 * SocketDemo.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2016 Mark F. Russo
 * 
 * This file is part of DoodlePad
 * 
 * DoodlePad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DoodlePad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DoodlePad.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.List;
import doodlepad.*;

public class SocketDemo {
    
    class SocketServer extends Pad {

        private final Oval o;

        public SocketServer()
        {
            super("Server", 300, 300, false);
            o = new Oval(10, 10, 50, 50, this.getLayer(0));
            o.setEventsEnabled(false);

            //listen();
        }

        public void listen() {
            startListening(4321);
        }

        public void printIP4s() {
            try {
                for (String ip: getIPv4Addresses()) {
                    System.out.println(ip);
                }
            } catch (java.net.SocketException ex) {
            }
        }

        public void printIP6s() {
            try {
                for (String ip: getIPv6Addresses()) {
                    System.out.println(ip);
                }
            } catch (java.net.SocketException ex) {
            }
        }

        @Override
        public void onMouseClicked(double x, double y, int b) {
            stopListening();
        }

        @Override
        public void onClientClosed(int id) {
            System.out.println("Client " + id + " closed");
        }

        @Override
        public void onClientOpened(int id) {
            System.out.println("Client " + id + " opened");
        }

        @Override
        public void onMouseMoved(double x, double y, int but) {
            o.setCenter(x, y);
            broadcast(x + "," + y);
        }

        @Override
        public void onClientReceived(int id, String msg) {
            String[] coords = msg.split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            //o.setCenter(x, y);
            o.setCenter(y, x);      // Swap coordinates for fun
        }
    }

    class SocketClient extends Pad
    {
        private final Oval o;

        public SocketClient() {
            super("Client", 300, 300, false);
            o = new Oval(10, 10, 50, 50, this.getLayer(0));
            o.setEventsEnabled(false);

            //connect();
        }

        public void connect() {
            // Get an IP address for the current machine
            String ip = "127.0.0.1";
            List<String> ips = null;

            try {
                ips = getIPv6Addresses();
            } catch (java.net.SocketException ex) {
            }

            if (ips == null) {
                System.out.println("Can't get IP addresses");
                return;
            }

            if (ips.size() > 0) ip = ips.get(0);

            openConnection(ip, 4321);
        }

        @Override
        public void onClientClosed(int id) {
            System.out.println("Client " + id + " closed");
        }

        @Override
        public void onClientOpened(int id) {
            System.out.println("Client " + id + " opened");
        }

        @Override
        public void onMouseClicked(double x, double y, int b) {
            closeAllConnections();
        }

        @Override
        public void onMouseMoved(double x, double y, int but) {
            o.setCenter(x, y);
            broadcast(x + "," + y);
        }

        @Override
        public void onClientReceived(int id, String msg) {
            String[] coords = msg.split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            o.setCenter(x, y);
        }
    }
    
    public SocketDemo() {
        SocketServer server = new SocketServer();
        SocketClient client = new SocketClient();
        
        server.listen();
        client.connect();
    }
    
    public static void main(String[] args) {
        SocketDemo st = new SocketDemo();
    }
}
