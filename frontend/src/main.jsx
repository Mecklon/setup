import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import AuthContextProvider from "./context/AuthContextProvider.jsx";
import { Provider } from "react-redux";
import {store} from './store/store.jsx'

createRoot(document.getElementById("root")).render(
  <Provider store={store}>
    <AuthContextProvider>
      <App />
    </AuthContextProvider>
  </Provider>
);
