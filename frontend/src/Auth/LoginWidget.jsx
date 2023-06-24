import React from "react";
import { Redirect } from "react-router-dom";
import { useOktaAuth } from "@okta/okta-react";
import OktaSigninWidget from "./OktaSigninWidget";

export const LoginWidget = ({ config }) => {
  const { oktaAuth, authState } = useOktaAuth();
  const onSuccess = (tokens) => {
    oktaAuth.handleLoginRedirect(tokens);
  };

  const onError = (error) => {
    console.log("Sign in error: ", error);
  };

  if (!authState) {
    return <div></div>;
  }

  return authState.isAuthenticated ? (
    <Redirect to={{ pathname: "/" }} />
  ) : (
    <OktaSigninWidget config={config} onSuccess={onSuccess} onError={onError} />
  );
};

export default LoginWidget;
