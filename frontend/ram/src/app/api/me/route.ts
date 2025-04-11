// app/api/me/route.ts
import { NextResponse } from 'next/server';
import { cookies } from "next/headers";
import { getIronSession } from "iron-session";
import { SessionData } from '@/app/lib/definitions';

const SESSION_KEY = process.env.SESSION_KEY;
const COOKIE_NAME = process.env.COOKIE_NAME;


export async function GET() {
    const session = await getIronSession<SessionData>(await cookies(), {
        password: SESSION_KEY,
        cookieName: COOKIE_NAME,
        });

  if (!session) {
    return NextResponse.json({ error: 'Not logged in' }, { status: 401 });
  }

  return NextResponse.json({ session });
}


