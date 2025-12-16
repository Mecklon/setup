import { useContext } from "react";
import { websocketContext } from "../context/WebSocketProvider";

const useWebsocketContext = () => {
  return useContext(websocketContext);
};
export default useWebsocketContext;
