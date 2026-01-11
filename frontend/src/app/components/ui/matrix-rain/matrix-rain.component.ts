import { Component, ElementRef, ViewChild, AfterViewInit, OnDestroy, NgZone, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-matrix-rain',
  standalone: true,
  imports: [CommonModule],
  template: `
    <canvas #canvas class="fixed top-0 left-0 w-full h-full -z-10 opacity-20 pointer-events-none"></canvas>
  `
})
export class MatrixRainComponent implements AfterViewInit, OnDestroy {
  @ViewChild('canvas') canvasRef!: ElementRef<HTMLCanvasElement>;
  private ngZone = inject(NgZone);
  private animationId: number | null = null;
  private resizeObserver: ResizeObserver | null = null;

  ngAfterViewInit() {
    this.initMatrixRain();
  }

  ngOnDestroy() {
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
    }
    if (this.resizeObserver) {
      this.resizeObserver.disconnect();
    }
  }

  private initMatrixRain() {
    const canvas = this.canvasRef.nativeElement;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const resizeCanvas = () => {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
    };
    
    resizeCanvas();
    
    // Re-ajustar si cambia el tamaño de la ventana
    this.resizeObserver = new ResizeObserver(() => resizeCanvas());
    this.resizeObserver.observe(document.body);

    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%^&*()';
    const fontSize = 14;
    const columns = Math.floor(canvas.width / fontSize);
    const drops: number[] = new Array(columns).fill(1);

    const draw = () => {
      // Fondo semi-transparente para efecto de estela
      ctx.fillStyle = 'rgba(0, 0, 0, 0.05)';
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      ctx.fillStyle = '#0F0'; // Verde Matrix
      ctx.font = `${fontSize}px monospace`;

      for (let i = 0; i < drops.length; i++) {
        const text = characters.charAt(Math.floor(Math.random() * characters.length));
        ctx.fillText(text, i * fontSize, drops[i] * fontSize);

        if (drops[i] * fontSize > canvas.height && Math.random() > 0.975) {
          drops[i] = 0;
        }
        drops[i]++;
      }

      this.animationId = requestAnimationFrame(draw);
    };

    // Ejecutar fuera de Angular Zone para no disparar detección de cambios constante
    this.ngZone.runOutsideAngular(() => {
      draw();
    });
  }
}
