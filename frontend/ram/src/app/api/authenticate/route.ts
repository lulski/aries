import { NextResponse } from "next/server";

export async function POST(request: Request) {
  const API_URL = process.env.API_AUTH_URL;
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;

  const body = await request.json();

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
  return NextResponse.json({ success: true, data });
}
