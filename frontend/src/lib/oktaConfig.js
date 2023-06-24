export const oktaConfig = {
  clientId: "0oa9f84i3mYeF77k45d7",
  issuer: "https://dev-88526197.okta.com/oauth2/default",
  redirectUri: "http://localhost:5173/login/callback",
  scopes: ["openid", "profile", "email"],
  pkce: true,
  disableHttpsCheck: true,
};
