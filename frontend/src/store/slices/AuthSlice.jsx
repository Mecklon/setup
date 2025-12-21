import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../../api/api";

let initialState = {
  username: null,
  autoLoginPending: false,
  autoLoginError: null,
};

export const autoLogin = createAsyncThunk("auth/autologin", async () => {
  const res = await api.get("/autoLogin");
  console.log(res.data)
  return res.data;
});

const AuthSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setUsername: (state, action) => {
      state.username = action.payload.username;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(autoLogin.pending, (state, action) => {
        state.autoLoginError = null;
        state.autoLoginPending = true;
      })
      .addCase(autoLogin.rejected, (state, action) => {
        state.autoLoginError = action.error.message;
        state.autoLoginPending = false;
      })
      .addCase(autoLogin.fulfilled, (state, action) => {
        state.autoLoginError = null;
        state.autoLoginPending = false;
        state.username = action.payload.username;
      });
  },
});

export const {setUsername} = AuthSlice.actions;
export default AuthSlice.reducer;
