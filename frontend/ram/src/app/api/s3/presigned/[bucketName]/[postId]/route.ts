import { NextRequest, NextResponse } from "next/server";

const API_PRESIGNED_URL = process.env.API_PRESIGNED_URL;

export async function POST(
    request: NextRequest,
    { params }: { params: Promise<{bucketName: string, postId: string}> }
) {
    const { bucketName, postId } = await params;

    console.info(">>> Requesting presigned URL for bucket: ", bucketName, " and postId: ", postId);

    const fetchUrl = `${API_PRESIGNED_URL}/${bucketName}/${postId}`;
    console.info(">>> Fetching presigned URL from: ", fetchUrl);

    const response = await fetch(fetchUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization:
                "Basic " +
                Buffer.from(`${process.env.API_USERNAME}:${process.env.API_PASSWORD}`).toString("base64"),
        },
    });

    if (!response.ok) {
        console.error("Failed to fetch presigned URL:", response.statusText);
        return NextResponse.json(
            { success: false, message: "Failed to fetch presigned URL" },
            { status: 500 }
        );
    }

    const data = await response.json();
    console.info("Received presigned URL:", data);

    return NextResponse.json({ success: true, data });
}