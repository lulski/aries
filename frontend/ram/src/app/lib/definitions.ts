import { z } from "zod";

export const LoginFormSchema = z.object({
  username: z
    .string()
    .min(3, { message: "username must be at least 3 characters long" })
    .trim(),
  password: z
    .string()
    .min(3, { message: "password must be at least 3 characters long" })
    .trim(),
});

export type LoginFormState =
  | {
      errors?: {
        username?: string[];
        password?: string[];
      };
      message?: string;
    }
  | undefined;

export type SessionData = {
  isAuthenticated: boolean;
  username: string;
  firstname: string;
  lastname: string;
  authorities: string[];
};

export type PostData = {
  id: string;
  title: string;
  content: string;
  author: string;
  createdOn: string;
  modifiedOn: string;
};
