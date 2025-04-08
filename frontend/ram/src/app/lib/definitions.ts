import {z} from 'zod';

export const LoginFormSchema = z.object({
    username: z.string().min(3, {message: 'username must be at least 3 characters long'}).trim(),
    password: z.string().min(3, {message: 'password must be at least 3 characters long'}).trim(),
})

export type LoginFormState = | {
    errors?: {
        username?: string[]
        password?: string[]
    }
    message?: string
} | undefined