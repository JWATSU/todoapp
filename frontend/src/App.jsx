import "./App.css";
import { TodoWrapper } from "./components/TodoWrapper";
import "bootstrap/dist/css/bootstrap.min.css";
import { Redirect, Route, Switch, useHistory } from "react-router-dom";
import { Navbar } from "./components/NavbarAndFooter/Navbar";
import { oktaConfig } from "./lib/oktaConfig";
import { OktaAuth, toRelativeUrl } from "@okta/okta-auth-js";
import {Security, LoginCallback, SecureRoute} from "@okta/okta-react";
import LoginWidget from "./Auth/LoginWidget";

const oktaAuth = new OktaAuth(oktaConfig);

export const App = () => {
  const customAuthHandler = () => {
    history.push("/login");
  };

  const history = useHistory();

  const restoreOriginalUri = async (_oktaAuth, originalUri) => {
    history.replace(toRelativeUrl(originalUri || "/", window.location.origin));
  };

  return (
    <>
      <Security
        oktaAuth={oktaAuth}
        restoreOriginalUri={restoreOriginalUri}
        onAuthRequired={customAuthHandler}
      >
        <Navbar />
        <Switch>
          <Route path="/" exact>
            <Redirect to="/home" />
          </Route>

          <SecureRoute path="/home">
            <TodoWrapper />
          </SecureRoute>
          <Route
            path="/login"
            render={() => <LoginWidget config={oktaConfig} />}
          />
          <Route path="/login/callback" component={LoginCallback} />
        </Switch>
      </Security>
    </>
  );
};

export default App;
