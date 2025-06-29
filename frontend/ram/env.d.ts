// env.d.ts
declare namespace NodeJS {
  interface ProcessEnv {
    API_AUTH_URL: string;
    API_POST_URL: string;
    API_USERNAME: string;
    API_PASSWORD: string;
    SESSION_KEY: string;
    COOKIE_NAME: string;
    // Add more vars here as needed
  }
}
