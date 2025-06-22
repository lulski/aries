import {
  fetchPost,
  fetchPostById,
  fetchPostByTitle,
} from "@/app/lib/fetchPost";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  { params }: { params: { param: string } }
) {
  const { param } = params;

  console.info(">>> request", request);
  console.info(">>> param", param);

  const isNumeric = /^\d+$/.test(param);

  let response;
  if (isNumeric) {
    // fetch by numeric ID
    response = await fetchPostById(param);
  } else {
    // fetch by slug
    response = await fetchPostByTitle(param);
  }

  if (response.success) {
    return NextResponse.json(response.data);
  } else {
    return NextResponse.json(
      { success: false, message: response.error },
      { status: 400 }
    );
  }
}
