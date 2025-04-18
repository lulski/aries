import { getIronSession } from "iron-session";
import { cookies } from "next/headers";
import { SessionData } from "@/app/lib/definitions";

const SESSION_KEY = process.env.SESSION_KEY!;
const COOKIE_NAME = process.env.COOKIE_NAME!;

export async function getSessionData(): Promise<SessionData> {
  const session = await getIronSession<SessionData>(await cookies(), {
    password: SESSION_KEY,
    cookieName: COOKIE_NAME,
  });
  return session;
}

export async function getUsername(): Promise<string> {
  const session = await getSessionData();
  return session.username;
}