import Signup from "./Signup";
import Login from "./Login";
import "./index.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { autoLogin } from "./store/slices/AuthSlice";

function App() {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(autoLogin());
  }, []);



  const { username } = useSelector((store) => store.auth);

  return (
    <BrowserRouter>
      {!username ? (
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Signup />} />
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      ) : (
        <Routes>
          <Route path="*" element={<div>App {username}</div>}></Route>
        </Routes>
      )}
    </BrowserRouter>
  );
}

export default App;
