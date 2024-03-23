import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./security/AuthContext";

export default function LoginComponent() {
  const [username, setUsername] = useState("DNA");
  const [password, setPassword] = useState("dummy");
  const [showErrorMessage, setShowErrorMessage] = useState(false);
  const authContext = useAuth();
  const navigate = useNavigate();

  function handleUsernameChange(event) {
    setUsername(event.target.value);
  }

  function handlePasswordChange(event) {
    setPassword(event.target.value);
  }

  function handleSubmit() {
    if (authContext.login(username, password)) {
      navigate(`/welcome/${username}`);
    } else {
      setShowErrorMessage(true);
    }
  }

  return (
    <div className="Login">
      <h1>Time to Login!</h1>
      <div className="successOrFail">
        {showErrorMessage && <div className="errorMessage">Authenticated Failed. Please check your credentials.</div>}
      </div>
      <div className="LoginForm">
        <div>
          <label>User Name:</label>
          <input type="text" name="username" value={username} onChange={handleUsernameChange}></input>
        </div>
        <div>
          <label>Password:</label>
          <input type="password" name="password" value={password} onChange={handlePasswordChange}></input>
        </div>
        <div>
          <button type="button" name="login" onClick={handleSubmit}>
            login
          </button>
        </div>
      </div>
    </div>
  );
}
