import type { Meta, StoryObj } from '@storybook/nextjs-vite';

import { expect, within } from "storybook/test";


import { DropZone } from './DropZone';

const meta = {
  title: "Components/DropZone",
  component: DropZone,
} satisfies Meta<typeof DropZone>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};

// interaction test ensures basic fields render correctly
Default.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  // title should be visible
  await expect(
    canvas.getByRole("heading", { name: /hello world/i }),
  ).toBeInTheDocument();
  // content from the editor should render
  await expect(canvas.getByText(/this is post content/i)).toBeInTheDocument();
  // author and date appear
  await expect(canvas.getByText(/alice/i)).toBeInTheDocument();
  await expect(canvas.getByText(/2026-03-08/)).toBeInTheDocument();
};
