import { fetchPost } from "@/app/lib/fetchPost";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  context: { params: Promise<{ id: string }> }
) {
  const { id } = await context.params;
  const result = await fetchPost(id);

  if (result.success) {
    return NextResponse.json(result.data);
  } else {
    return NextResponse.json(
      { success: false, message: result.error },
      { status: 400 }
    );
  }
}
