// env.d.ts
declare namespace NodeJS {
    interface ProcessEnv {
      API_AUTH_URL: string;
      API_POST_URL: string;
      SESSION_KEY: string;
      COOKIE_NAME: string;
      // Add more vars here as needed
    }
  }
  