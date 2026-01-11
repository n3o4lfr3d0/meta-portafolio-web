import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-logo',
  standalone: true,
  template: `
    <div class="font-mono font-bold tracking-tighter select-none flex items-center" [class]="sizeClass">
      <span class="text-default">&lt;</span>
      <span class="text-default">n</span>
      <span class="text-yellow-600 dark:text-yellow-400">3</span>
      <span class="text-default">o</span>
      <span class="text-yellow-600 dark:text-yellow-400">4</span>
      <span class="text-default">lfr</span>
      <span class="text-yellow-600 dark:text-yellow-400">3</span>
      <span class="text-default">d</span>
      <span class="text-yellow-600 dark:text-yellow-400">0</span>
      <span class="text-default"> /&gt;</span>
      <span class="ml-1 w-2 h-5 bg-yellow-600 dark:bg-yellow-400 animate-pulse" [class.h-4]="size === 'sm'" [class.h-6]="size === 'lg'"></span>
    </div>
  `,
  styles: []
})
export class LogoComponent {
  @Input() size: 'sm' | 'md' | 'lg' = 'md';

  get sizeClass() {
    switch (this.size) {
      case 'sm': return 'text-lg';
      case 'lg': return 'text-3xl';
      default: return 'text-xl'; // md
    }
  }
}
