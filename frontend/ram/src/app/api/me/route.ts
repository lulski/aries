// app/api/me/route.ts
import { SessionData } from "@/app/lib/definitions";
import { getIronSession } from "iron-session";
import { cookies } from "next/headers";
import { NextResponse } from "next/server";

const SESSION_KEY = process.env.SESSION_KEY;
const COOKIE_NAME = process.env.COOKIE_NAME;

export async function GET() {
  const session = await getIronSession<SessionData>(await cookies(), {
    password: SESSION_KEY,
    cookieName: COOKIE_NAME,
  });

  if (!session.isAuthenticated) {
    return NextResponse.json({ error: "Not authenticated" }, { status: 401 });
  }

  return NextResponse.json({ session });
}
