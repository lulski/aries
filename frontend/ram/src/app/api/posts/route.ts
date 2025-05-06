import { LoginFormSchema, SessionData } from "@/app/lib/definitions";
import { fetchPost } from "@/app/lib/fetchPost";
import { getIronSession } from "iron-session";
import { cookies } from "next/headers";
import { NextResponse } from "next/server";
import { z } from "zod";

export async function POST(request: Request) {
  const API_URL = process.env.API_POST_URL;
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;

  const body = await request.json();
  console.log("Received from client:", body);

  const NewPostSchema = z.object({
    title: z
      .string()
      .min(3, { message: "Title must be at least 3 characters long" })
      .trim(),
  });

  const validatedField = NewPostSchema.safeParse(body);

  if (!validatedField.success) {
    const issues = validatedField.error.errors;
    const messages = issues.map((issue) => issue.message);

    return NextResponse.json(
      { success: false, message: messages },
      { status: 403 }
    );
  }

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
    const messages: string[] = ["invalid credentials"];

    return NextResponse.json(
      { success: false, message: messages },
      { status: 401 }
    );
  }

  const data = await response.json();

  return NextResponse.json({ success: true, data });
}
