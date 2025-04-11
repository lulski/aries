import { NextResponse } from "next/server";
import { LoginFormSchema, LoginFormState } from "@/app/lib/definitions";
import { cookies } from "next/headers";
import { getIronSession } from "iron-session";
import { log } from "console";
import { SessionData } from "@/app/lib/definitions";

export async function POST(request: Request) {
  const API_URL = process.env.API_AUTH_URL;
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;
  const SESSION_KEY = process.env.SESSION_KEY;
  const COOKIE_NAME = process.env.COOKIE_NAME;

  
  const body = await request.json();

  const validatedField = LoginFormSchema.safeParse(body);


  if (!validatedField.success) {
    const issues = validatedField.error.errors;
    const messages = issues.map((issue) => issue.message );

    return NextResponse.json(
      { success: false, message: messages },
      { status: 403 }
    );
  }

  console.log("Received from client:", body);

  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      Authorization:
        "Basic " + Buffer.from(`${USERNAME}:${PASSWORD}`).toString("base64"),
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    return NextResponse.json(
      { success: false, message: "Authentication failed" },
      { status: 401 }
    );
  }
  

  const data = await response.json();
  const session = await getIronSession<SessionData>(await cookies(), {password: SESSION_KEY, cookieName: COOKIE_NAME});
  session.username = data.username;
  session.firstname = data.firstName;
  session.lastname = data.lastName;
  session.authorities = data.roles;
  await session.save();  
  return NextResponse.json({ success: true, data });
}
