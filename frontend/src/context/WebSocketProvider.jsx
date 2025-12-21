import { useState,createContext, useEffect, useRef } from "react";
import { useWebSocket } from "../hooks/useWebSocket";

export const websocketContext = createContext();

const WebSocketProvider = ({ children }) => {
  const ws = useWebSocket(localStorage.getItem("JwtToken"));
  const clientRef = useRef(null);
  if (!clientRef.current) {
    clientRef.current = ws; // stable forever
  }
  const [wsConnected, setWsConnected] = useState(false);

  useEffect(() => {
    let client = clientRef.current;
    client.onConnect = () => {
      console.log("Websocket connected");
      setWsConnected(true);
    };
    client.onDisconnect = () => {
      console.log("❌ Disconnected from WebSocket");
    };

    client.activate();
    return () => {
      client.deactivate();
      setWsConnected(false);
    };
  }, []);

  useEffect(() => {
    if (clientRef.current == null || !wsConnected) return;
  }, [wsConnected]);

  return (
    <websocketContext.Provider value={clientRef.current}>
        {children}
    </websocketContext.Provider>
  );
};

export default WebSocketProvider;
